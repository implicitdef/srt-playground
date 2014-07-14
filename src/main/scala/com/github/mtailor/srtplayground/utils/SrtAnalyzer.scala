package com.github.mtailor.srtplayground.utils

import com.github.mtailor.srtdissector.Vocabulary._

trait SrtAnalyzer {

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


  private def duration(s: SubtitleBlock) =
    s.end - s.start


}
