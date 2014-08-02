package com.github.mtailor.srtplayground.reorg.helpers

import java.io.{File, FileOutputStream}
import java.nio.file.Files

import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.io.FileUtils

class FilesToolbox extends LazyLogging {

  def writeToNewFile(bytes: Array[Byte], path: String): Unit = {
    logger.info(f"writing $path")
    val fos = new FileOutputStream(path)
    try
      fos.write(bytes)
    finally
      fos.close
  }

  def filesInFolder(path: String): Iterable[File] =
    path.listFiles

  def makeDir(path: String): Unit = {
    logger.info(f"mkdir $path")
    path.mkdir()
  }


  def deleteDir(path: String): Unit = {
    logger.info(f"removing $path")
    FileUtils.deleteDirectory(path)
  }

  def moveFile(src: String, dest: String): Unit = {
    logger.info(f"moving $src to $dest")
    Files.move(src.toPath, dest.toPath)
  }

  def containsOneFile(path: String): Boolean =
    filesInFolder(path).size == 1

  private implicit def string2File(s: String): File =
    new File(s)

}

