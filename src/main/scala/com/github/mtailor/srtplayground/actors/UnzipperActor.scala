package com.github.mtailor.srtplayground.actors

import java.nio.file.Files._
import java.nio.file.{StandardCopyOption, Path}

import akka.actor.Actor
import com.github.mtailor.srtplayground.ActorPaths
import com.github.mtailor.srtplayground.helpers.PathConversions._
import net.lingala.zip4j.core.ZipFile

class UnzipperActor extends Actor {
  import context._
  override def receive = {
    //unzip the given zip and places the contained file
    //at a temporary location, which is sent back
    //the zip SHOULD contain exactly 1 file, otherwise an exception
    //is thrown
    case zipPath: Path => {
      val tempDir = createTempDirectory(null)
      new ZipFile(zipPath).extractAll(tempDir.getAbsolutePath)
      val nbFiles = tempDir.listFiles.length
      if(nbFiles != 1)
        throw new IllegalArgumentException(s"The zip at ${zipPath} contained ${nbFiles} file(s)")
      val containedFile = tempDir.listFiles.head
      if(containedFile.isDirectory)
        throw new IllegalArgumentException(s"The zip at ${zipPath} contained a directory")
      val targetPath = createTempFile(null, null)
      copy(containedFile, targetPath, StandardCopyOption.REPLACE_EXISTING)
      //send back the location
      sender ! targetPath
      //delete the temp dir
      actorSelection(ActorPaths.filesDeletionActor) ! tempDir
    }
  }

}
