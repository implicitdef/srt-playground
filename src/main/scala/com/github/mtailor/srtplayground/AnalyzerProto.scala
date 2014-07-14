package com.github.mtailor.srtplayground

import java.io._

import com.github.mtailor.srtdissector.SrtDissector
import com.github.mtailor.srtdissector.Vocabulary.Srt
import com.github.mtailor.srtplayground.utils.{LevenhsteinDistance, SrtCleaner}

object AnalyzerProto extends App with SrtCleaner with SrtDissector with LevenhsteinDistance {

  val files: Array[File] = new File("/home/manu/dev/writes").listFiles

  def takeNChars(srt: Srt, n: Int): String =
    srt
      .flatMap(_.lines)
      .mkString("\n")
      .take(n)

  val combinationsAndSimilarityRate: Seq[(Int, Int, Float)] =
    files
      .map(f =>
      (f.getName.replaceAll( """^(\d+).*$""", "$1").toInt,
        takeNChars(dissect(new FileInputStream(f)), 1000)
        )
      )
      .combinations(2)
      .map { case Array((id1, s1), (id2, s2)) =>
        (id1, id2, similarityRate(s1, s2))
      }
      .toSeq

  private val veryHighMatches =
    combinationsAndSimilarityRate
      .filter(
      _._3 >= 0.85
    ).map{ case (id1, id2, _) =>
      (id1, id2)
    }
  print(veryHighMatches)



  val allIds = (1 to 12).toSeq

  val initialSetOfSets: Set[Set[Int]] = allIds.map(x => Set(x)).toSet



  val clusters: Set[Set[Int]] = veryHighMatches.foldLeft(initialSetOfSets)(mergeSubSets)


  println("--CLUSTERS : ------------")
  clusters foreach println






  def print(matches: Seq[(Int, Int)] = veryHighMatches) {
    matches foreach println
  }






  def mergeSubSets[T](setOfSets: Set[Set[T]], coupleToMerge: (T, T)): Set[Set[T]] = {
    val (a, b) = coupleToMerge
    val subsetA = findSubset(setOfSets, a)
    val subsetB = findSubset(setOfSets, b)
    if(subsetA == subsetB)
      setOfSets
    else
      setOfSets + (subsetA ++ subsetB) - subsetA - subsetB
  }

  def findSubset[T](setOfSets: Set[Set[T]], a: T): Set[T] =
    setOfSets.find(_.contains(a)).getOrElse (
      throw new RuntimeException(f"the value $a was not contained in any subset of $setOfSets")
    )






}
