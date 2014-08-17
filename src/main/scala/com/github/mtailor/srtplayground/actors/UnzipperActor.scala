package com.github.mtailor.srtplayground.actors

import java.nio.file.Files._
import java.nio.file.{Path, StandardCopyOption}

import akka.actor.{Actor, ActorLogging}
import com.github.mtailor.srtplayground.helpers.ActorPaths
import com.github.mtailor.srtplayground.helpers.PathConversions._
import com.github.mtailor.srtplayground.helpers.VariousConstants._
import net.lingala.zip4j.core.ZipFile

class UnzipperActor extends Actor with ActorLogging {
  import context._
  override def receive = {
    //unzip the given zip and places the contained file
    //at a temporary location, which is sent back in an Option
    //the zip SHOULD contain exactly 1 file, otherwise None is sent back
    case zipPath: Path => {
      val tempDir = createTempDirectory(tempFilesPrefix)
      new ZipFile(zipPath).extractAll(tempDir.getAbsolutePath)
      val nbFiles = tempDir.listFiles.length
      // response
      sender ! {
        if(tempDir.listFiles.length == 1 && ! tempDir.listFiles.head.isDirectory) {
          val targetPath = createTempFile(tempFilesPrefix, tempFilesPrefix)
          copy(tempDir.listFiles.head, targetPath, StandardCopyOption.REPLACE_EXISTING)

          Some(targetPath)
        } else {
          log.warning(s"The zip $zipPath contained less or more than 1 file, or that file was a directory")
          None
        }
      }
      //delete the temp dir
      actorSelection(ActorPaths.filesDeletionActor) ! tempDir
    }
  }

}
