package com.github.mtailor.srtplayground

import com.github.mtailor.srtplayground.utils.{ClusteringAlgorithm, FilesToolbox, LevenhsteinDistance, SrtToolbox}

object SubtitlesComparerProto
  extends App
  with LevenhsteinDistance
  with ClusteringAlgorithm
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

  val clustersCriterion = (a: Int, b: Int) =>
    similarityRate(
      filesBeginningsById(a),
      filesBeginningsById(b)
    ) >= 0.85

  val clusters = computeClusters(filesBeginningsById.keySet, clustersCriterion)


  println("-- GROUPING OF THE .srt FILES BY SIMILARITY --")
  clusters foreach println


}
