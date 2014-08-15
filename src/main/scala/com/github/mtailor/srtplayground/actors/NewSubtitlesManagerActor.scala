package com.github.mtailor.srtplayground.actors

import java.nio.file.Path

import akka.actor.{Actor, ActorLogging}
import com.github.mtailor.srtdissector.Vocabulary.Srt
import com.github.mtailor.srtplayground.helpers.PathConversions._
import com.github.mtailor.srtplayground.helpers._

import scala.util.control.NonFatal

object SubtitlesAnalysisSignal

class NewSubtitlesManagerActor(
  val basicClusteringHelper: BasicClusteringHelper,
  val srtHelper: SrtHelper,
  val stringsComparisonHelper: StringsComparisonHelper
) extends Actor with ActorLogging {

  val srts: scala.collection.mutable.Map[Path, Srt] = scala.collection.mutable.Map.empty
  val similarityThreshold = 0.85

  override def receive = {
    case newSubtitleTempFilePath: Path =>
      log.info(s"received a new file : $newSubtitleTempFilePath")
      //TODO better manage the parsing problems : the actor gets killed and the srts variable is empty again...
      try {
        srts put(newSubtitleTempFilePath, srtHelper.readSrt(newSubtitleTempFilePath))
      } catch {
        case NonFatal(t) => log.error(t, "parsing problem")
      }
    case SubtitlesAnalysisSignal =>
      val beginnings = srts mapValues (srtHelper.firstChars(_, 100))

      var cpt = 0

      val shouldBeRegrouped: ((Any, String), (Any, String)) => Boolean =
        (a, b) => stringsComparisonHelper.similarityRate(a._2, b._2) >= similarityThreshold



      val groups = basicClusteringHelper.group(
        beginnings.toSet,
        shouldBeRegrouped
      )

      log.info("----------------------")
      log.info(s"---Found ${groups.size} groups based on ${srts.size} files : ")
      groups foreach { g =>
        log.info(s" - (${g.size}) ${g.map(_._1)}")
      }


  }


}
