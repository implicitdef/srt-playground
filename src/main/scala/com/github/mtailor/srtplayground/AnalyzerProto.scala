package com.github.mtailor.srtplayground

import java.io._

import com.github.mtailor.srtdissector.SrtDissector
import com.github.mtailor.srtdissector.Vocabulary.Srt
import com.github.mtailor.srtplayground.utils.{LevenhsteinDistance, SrtCleaner}

object AnalyzerProto extends App with SrtCleaner with SrtDissector with LevenhsteinDistance {

  val files: Array[File] = new File("/home/manu/dev/writes").listFiles


  def beginning(srt: Srt): String =
    srt
      .take(20)
      .flatMap(_.lines)
      .mkString

  val t = files
    .map(f =>
      (
        f.getName,
        beginning(dissect(new FileInputStream(f)))
      )
    )
    .combinations(2)
    .map { case Array((name1, s1), (name2, s2)) =>
      (similarityRate(s1, s2), f"$name1 => $name2")
    }
    .toSeq
    .sortBy(_._1)
    .foreach(println)

}
