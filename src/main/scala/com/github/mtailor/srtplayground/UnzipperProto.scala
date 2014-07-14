package com.github.mtailor.srtplayground

import com.github.mtailor.srtplayground.utils.FilesToolbox
import net.lingala.zip4j.core.ZipFile;

object UnzipperProto extends App with FilesToolbox {

  1 to 13 foreach { id =>
    new ZipFile(f"/home/manu/dev/writes/$id%04d.zip")
      .extractAll("/home/manu/dev/writes/")
  }


}
