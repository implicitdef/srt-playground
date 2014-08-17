package com.github.mtailor.srtplayground.actors

import java.nio.file.Path

import akka.actor.{Actor, ActorLogging}
import com.github.mtailor.srtdissector.Vocabulary._
import com.github.mtailor.srtplayground.actors.NewSubtitlesManagerActor.SubtitlesDumpingSignal
import com.github.mtailor.srtplayground.analysis.SrtFullComparisonHelper
import com.github.mtailor.srtplayground.analysis.SrtFullComparisonHelper.{ZeroShift, SameTextShiftedTimings, TimingShift}
import com.github.mtailor.srtplayground.helpers.PathConversions._
import com.github.mtailor.srtplayground.helpers._

import scala.collection.mutable.{ListBuffer, Map => MutableMap}
import scala.util.control.NonFatal

object NewSubtitlesManagerActor {

  object SubtitlesDumpingSignal

}

class NewSubtitlesManagerActor(
  val basicClusteringHelper: BasicClusteringHelper,
  val srtHelper: SrtHelper,
  val srtFullComparisonHelper: SrtFullComparisonHelper
) extends Actor with ActorLogging with StandardParameters {

  //the srts are regrouped by textual similarity
  //then by variation of the timings
  val files = MutableMap[Srt, MutableMap[TimingShift, ListBuffer[Path]]]()

  override def receive = {
    case newSubtitleTempFilePath: Path =>
      log.info(s"received a new file : $newSubtitleTempFilePath")
      //TODO better manage the parsing problems : the actor gets killed and the srts variable is empty again...
      try {
        placeNewFile(srtHelper.readSrt(newSubtitleTempFilePath), newSubtitleTempFilePath)
      } catch {
        case NonFatal(t) => log.error(t, "parsing problem")
      }
    case SubtitlesDumpingSignal =>
      log.info("----------------------")
      log.info(s"-Found ${files.size} textual variations from ${files.flatMap(_._2).flatMap(_._2).size} files ")
      files.foreach { case (_, mapByShifts) =>
        log.info(s"--A group got ${mapByShifts.size} different shifts, for ${mapByShifts.flatMap(_._2).size} files")
        mapByShifts foreach { case (shift, paths) =>
          log.info(s"---The shift $shift got ${paths.size} files : $paths")
        }
      }
  }

  private def placeNewFile(newSrt: Srt, path: Path): Unit = {
    files
      .map { case (srt, rest) =>
      //add in the result of the comparison
      (srt, (srtFullComparisonHelper.compare(srt, newSrt), rest))
    }
    //find the first with the same text and a shift
    .find {
      case (srt, (_: SameTextShiftedTimings, _)) => true
      case _ => false
    } match {
      case None =>
        //brand new
        files(newSrt) = MutableMap(ZeroShift -> ListBuffer(path))
      case Some((srt, (SameTextShiftedTimings(foundShift), mapByShifts))) =>
        mapByShifts
          .find {
          case (shift, _) => shift == foundShift
        } match {
          case None =>
            //new shift
            mapByShifts(foundShift) = ListBuffer(path)
          case Some((_, paths: ListBuffer[Path])) =>
            paths += path
        }
    }
  }

}
