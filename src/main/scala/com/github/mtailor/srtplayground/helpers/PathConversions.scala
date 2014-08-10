package com.github.mtailor.srtplayground.helpers

import java.io.File
import java.nio.file.Path

object PathConversions {

  implicit def path2File(p: Path): File =
    p.toFile

  implicit def file2Path(f: File): Path =
    f.toPath

}
