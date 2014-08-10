package com.github.mtailor.srtplayground.helpers

import java.io.{FileInputStream, File}

import com.github.mtailor.srtdissector.SrtDissector
import com.github.mtailor.srtdissector.Vocabulary._
import com.typesafe.scalalogging.LazyLogging


class SrtHelper extends LazyLogging {

  def readSrt(f: File): Srt = {
    val srt = clean(SrtDissector(new FileInputStream(f)).getOrElse(
      throw new RuntimeException("Couldn't parse the file " + f)
    ))
    srt
  }

  def firstChars(srt: Srt, nbChars: Int): String =
    srt.flatMap(_.lines).mkString("\n").take(nbChars)

  //Removes the frequent 'ads' at the start and end of subtitles
  private def clean(srt: Srt): Srt =
    srt
      .zipWithIndex
      .filterNot { case (block, i) =>
        isIndexInHeadOrTail(srt, i, 3) &&
        isLikelyGarbage(block)
      }
      .map(_._1)


  private def isIndexInHeadOrTail(seq: Seq[_], index: Int, headAndTailSizes: Int) =
    index < headAndTailSizes ||
      index >= seq.length - headAndTailSizes

  private def isLikelyGarbage(b: SubtitleBlock): Boolean =
    b.lines.exists(isLikelyGarbage(_))

  private def isLikelyGarbage(s: String) = {
    val s2 = s.toLowerCase
    s2.contains("forom.com") ||
      s2.contains("www.addic7ed.com") ||
      s2.contains("www.tvsubtitles.net") ||
      s2.contains("www.opensubtitles.org") ||
      s2.contains("www.allsubs.org") ||
      s2.contains("uksubtitles.ru") ||
      s2.contains("open subtitles mkv player") ||
      (
        s2.contains("by") &&
          (
            s2.matches(".*sync.*") ||
            s2.matches(".*correct.*") ||
            s2.matches(".*subtitles.*") ||
            s2.matches(".*shared.*")
          )
        )
  }

}
