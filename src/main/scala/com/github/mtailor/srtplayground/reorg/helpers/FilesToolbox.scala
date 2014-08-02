package com.github.mtailor.srtplayground.reorg.helpers

import java.io.{File, FileOutputStream}
import java.nio.file.Files

import org.apache.commons.io.FileUtils

trait FilesToolbox {

  def writeToNewFile(bytes: Array[Byte], path: String): Unit

  def filesInFolder(path: String): Iterable[File]

  def makeDir(path: String): Unit

  def deleteDir(path: String): Unit

  def moveFile(src: String, dest: String): Unit

  def containsOneFile(path: String): Boolean
}

object FilesToolbox extends FilesToolbox {

  override def writeToNewFile(bytes: Array[Byte], path: String): Unit = {
    println(f"writing $path")
    val fos = new FileOutputStream(path)
    try
      fos.write(bytes)
    finally
      fos.close
  }

  override def filesInFolder(path: String): Iterable[File] =
    path.listFiles

  override def makeDir(path: String): Unit = {
    println(f"mkdir $path")
    path.mkdir()
  }


  override def deleteDir(path: String): Unit = {
    println(f"removing $path")
    FileUtils.deleteDirectory(path)
  }

  override def moveFile(src: String, dest: String): Unit = {
    println(f"moving $src to $dest")
    Files.move(src.toPath, dest.toPath)
  }
  override def containsOneFile(path: String): Boolean =
    filesInFolder(path).size == 1

  private implicit def string2File(s: String): File =
    new File(s)

}

