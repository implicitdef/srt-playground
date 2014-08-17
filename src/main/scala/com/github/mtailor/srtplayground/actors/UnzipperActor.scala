package com.github.mtailor.srtplayground.actors

import java.nio.file.Files._
import java.nio.file.{Path, StandardCopyOption}

import com.github.mtailor.srtplayground.actors.MonitoringActor.SubtitleProcessFailure
import com.github.mtailor.srtplayground.helpers.BaseActor
import net.lingala.zip4j.core.ZipFile

class UnzipperActor extends BaseActor {
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
          monitoringActor ! SubtitleProcessFailure
          None
        }
      }
      //delete the temp dir
      filesDeletionActor ! tempDir
    }
  }

}
