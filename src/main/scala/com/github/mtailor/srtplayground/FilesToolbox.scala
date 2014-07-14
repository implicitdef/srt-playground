package com.github.mtailor.srtplayground

import java.io.{File, FileOutputStream}

trait FilesToolbox {

  private var cpt = 0

  def writeToNewFile(bytes: Array[Byte]): Unit = {
    cpt += 1
    writeToFile(bytes, filePathForId(cpt))
  }

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
