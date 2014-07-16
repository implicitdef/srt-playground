package com.github.mtailor.srtplayground

import java.io._

import com.github.mtailor.srtdissector.SrtDissector
import com.github.mtailor.srtdissector.Vocabulary.Srt
import com.github.mtailor.srtplayground.utils.{ClusteringAlgorithm, LevenhsteinDistance, SrtCleaner}

object AnalyzerProto extends App with SrtCleaner with SrtDissector with LevenhsteinDistance with ClusteringAlgorithm[Int] {

  val files: Array[File] = new File("/home/manu/dev/writes").listFiles

  def takeNChars(srt: Srt, n: Int): String =
    srt
      .flatMap(_.lines)
      .mkString("\n")
      .take(n)


  val filesIdsAndBeginnings: Map[Int, String] =
    files
      .map (f =>
        (
          f.getName.replaceAll( """^(\d+).*$""", "$1").toInt,
          takeNChars(dissect(new FileInputStream(f)), 1000)
        )
      ).toMap

  val ids = filesIdsAndBeginnings.keySet

  def clustersCriterion(a: Int, b: Int) =
    similarityRate(
      filesIdsAndBeginnings(a),
      filesIdsAndBeginnings(b)
    ) >= 0.85

  private val clusters = computeClusters(ids, clustersCriterion)


  println("--CLUSTERS : ------------")
  clusters foreach println










}
