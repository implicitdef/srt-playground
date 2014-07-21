package com.github.mtailor.srtplayground

import com.github.mtailor.srtplayground.reorg.BasicClustering.group
import com.github.mtailor.srtplayground.utils.{FilesToolbox, StringsSimilarityHelper, SrtToolbox}

object MainProto
  extends App
  with StringsSimilarityHelper
  with SrtToolbox
  with FilesToolbox {

  val filesBeginningsById: Map[Int, String] =
    filesInFolder("/home/manu/dev/writes")
      .map (f =>
        (
          readId(f),
          firstChars(readSrt(f), 1000)
        )
      ).toMap

  val shouldBeInSameGroup = (a: Int, b: Int) =>
    similarityRate(
      filesBeginningsById(a),
      filesBeginningsById(b)
    ) >= 0.85

  val groups = group(filesBeginningsById.keySet, shouldBeInSameGroup)


  println("-- GROUPING OF THE .srt FILES BY SIMILARITY --")
  groups foreach println


}
