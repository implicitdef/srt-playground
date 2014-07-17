package com.github.mtailor.srtplayground.utils

import com.github.mtailor.srtdissector.Vocabulary._

trait SrtToolbox {

  /**
   * Returns the first n characters of the texts
   * of these subtitles
   */
  def firstChars(srt: Srt, nbChars: Int): String =
    srt.flatMap(_.lines).mkString("\n").take(nbChars)

  /**
   * Attempts to guess the overall amount of time spent
   * in dialogues in a media, approximated by the duration
   * of the subtitles compared to the whole media duration
   */
  def talkinessRateOfMedia(srt: Srt): Float =
    mediaDuration(srt) match {
      case 0 => throw new IllegalArgumentException("Can't compute rate for media lasting 0 ms")
      case m => subtitlesCumulativeDuration(srt).toFloat / m
    }

  /**
   * Returns the total duration of the media covered by
   * this .srt file.
   * approximated by as the time between 0 and the end of the last subtitle
   * i.e. if the actual media lasted long after the last subtitle
   * it will not be taken into account
   */
  def mediaDuration(srt: Srt): Int = srt match {
    case Seq() => 0
    case _ => srt.last.end
  }

  /**
   * Returns the total duration of the media where
   * subtitles will be active.
   * i.e. the sum of the duration of all subtitles
   */
  def subtitlesCumulativeDuration(srt: Srt): Int =
    srt.map(duration(_)).sum

  /**
   * Removes the spam lines often found
   * in the beginning and the end,
   * indicating the website/author of the ile
   */
  def clean(srt: Srt): Srt =
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

  private def duration(s: SubtitleBlock) =
    s.end - s.start

}
