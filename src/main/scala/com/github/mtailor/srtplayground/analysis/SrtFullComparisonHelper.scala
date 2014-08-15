package com.github.mtailor.srtplayground.analysis

import com.github.mtailor.srtdissector.Vocabulary.{SubtitleBlock, Time, Srt}
import com.github.mtailor.srtplayground.analysis.SrtFullComparisonHelper._
import com.github.mtailor.srtplayground.analysis.SrtsTextualMatchingHelper.TextualMatchingParameters
import Math._

object SrtFullComparisonHelper {


  case class FullComparisonParameters(
    textualMatchingParams: TextualMatchingParameters,
    minimumMatchingBlocksRate: Double,
    timingsShiftApproximation: Time
  )

  case class TimingShift(delay: Int, factor: Double)


  sealed trait SrtsFullComparisonResult
  //the texts are different, they must come from two different transcriptions
  //(or are not even for the same media)
  object Unrelated extends SrtsFullComparisonResult
  //the texts are near identical
  sealed trait SameText extends SrtsFullComparisonResult
  //the texts are near identical but the timings have nothing in common (should not be common)
  object SameTextUnrelatedTimings extends SameText
  //the texts are near identical and the timings are similar with a shift
  case class SameTextShiftedTimings(shift: TimingShift) extends SameText
  //the texts are near identical and the timings as well
  object SameTextSameTimings extends SameText

}
class SrtFullComparisonHelper(val textualComparisonHelper: SrtsTextualMatchingHelper) {

  /**
   * Compares two .srt files
   * both on their texts and their timings
   *
   */
  def compare(base: Srt, other: Srt)(implicit params: FullComparisonParameters): SrtsFullComparisonResult = {
    val matchingBlocks = textualComparisonHelper.computeMatches(base, other)(params.textualMatchingParams)
    val idealNbOfMatchingBlocks = min(max(base.size, other.size), params.textualMatchingParams.blocksToConsiderFromBeginning)
    if (matchingBlocks.size < 2 || matchingBlocks.size/idealNbOfMatchingBlocks < params.minimumMatchingBlocksRate)
      Unrelated
    else {
      //use the first block and the last to determine the shift
      val shift = computeShift(
        matchingBlocks.head._1.start,
        matchingBlocks.last._1.end,
        matchingBlocks.head._2.start,
        matchingBlocks.last._2.end
      )
      //check it on all blocks
      if(verifyShift(matchingBlocks, shift, params.timingsShiftApproximation))
        shift match {
          case TimingShift(0, 0) => SameTextSameTimings
          case _ => SameTextShiftedTimings(shift)
        }
      else
        SameTextUnrelatedTimings
    }
  }


  private def computeShift(baseStart: Time, baseEnd: Time, otherStart: Time, otherEnd: Time): TimingShift = {
    val factor: Double = (otherEnd - otherStart).toDouble / (baseEnd - baseStart)
    val delay: Int = (otherEnd - baseEnd * factor).toInt
    TimingShift(delay, factor)
  }


  private def verifyShift(
    matchingBlocks: Seq[(SubtitleBlock, SubtitleBlock)],
    shift: TimingShift,
    tolerance: Int
  ): Boolean =
    matchingBlocks.forall { case (baseBlock, otherBlock) =>
      matchesWithShift(baseBlock.start, otherBlock.end,  shift, tolerance) &&
      matchesWithShift(baseBlock.end, otherBlock.end, shift, tolerance)
    }


  private def matchesWithShift(base: Time, other: Time, shift: TimingShift, tolerance: Int) =
    abs(applyShift(base, shift) - other) <= tolerance

  private def applyShift(time: Time, shift: TimingShift): Time =
    (time * shift.factor + shift.delay).toInt





}
