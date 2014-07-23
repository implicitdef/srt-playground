package com.github.mtailor.srtplayground.utils

import java.io.{FileInputStream, File, FileOutputStream}

import com.github.mtailor.srtdissector.SrtDissector

trait FilesToolbox
  extends SrtToolbox {

  private var cpt = 0

  def writeToNewFile(bytes: Array[Byte]): Unit = {
    cpt += 1
    writeToFile(bytes, filePathForId(cpt))
  }

  def filesInFolder(path: String) =
    new File(path).listFiles

  def readSrt(f: File) =
    clean(SrtDissector(new FileInputStream(f)).getOrElse(
      throw new RuntimeException("Couldn't parse the file " + f)
    ))


  def readId(f: File) =
    f.getName.replaceAll( """^(\d+).*$""", "$1").toInt



  private def writeToFile(bytes: Array[Byte], filePath: String) = {
    println(f"writing into file $filePath")
    val fos = new FileOutputStream(new File(filePath))
    try {
      fos.write(bytes)
    } finally {
      fos.close()
    }
  }

  private def filePathForId(id: Int) =
    f"/home/manu/dev/writes/$id%04d.zip"




}
