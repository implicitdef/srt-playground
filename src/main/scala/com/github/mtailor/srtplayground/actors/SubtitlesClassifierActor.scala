package com.github.mtailor.srtplayground.actors

import java.nio.file.Path

import akka.actor.ReceiveTimeout
import akka.pattern.ask
import com.github.mtailor.srtdissector.Vocabulary._
import com.github.mtailor.srtplayground.actors.MonitoringActor.SubtitleProcessSuccess
import com.github.mtailor.srtplayground.actors.SubtitlesClassifierActor.SubtitlesDumpingSignal
import com.github.mtailor.srtplayground.analysis.SrtFullComparisonHelper
import com.github.mtailor.srtplayground.analysis.SrtFullComparisonHelper.{SameTextShiftedTimings, TimingShift, ZeroShift}
import com.github.mtailor.srtplayground.helpers._

import scala.collection.mutable.{ListBuffer, Map => MutableMap}
import scala.concurrent.duration._

object SubtitlesClassifierActor {

  object SubtitlesDumpingSignal

}

class SubtitlesClassifierActor(
  val srtFullComparisonHelper: SrtFullComparisonHelper
) extends BaseActor {

  //the srts are regrouped by textual similarity
  //then by variation of the timings
  val files = MutableMap[Srt, MutableMap[TimingShift, ListBuffer[Path]]]()

  override def receive = {
    case newSubtitleTempFilePath: Path =>
      log.info(s"received a new file : $newSubtitleTempFilePath")
      (parsingActor ? newSubtitleTempFilePath)
        .mapTo[Srt]
        .foreach { srt =>
          placeNewFile(srt, newSubtitleTempFilePath)
          monitoringActor ! SubtitleProcessSuccess
        }
    case SubtitlesDumpingSignal =>
      log.info("----------------------")
      val allPaths = files.map(_._2).flatten.flatMap(_._2)
      log.info(s"-Found ${files.size} textual variations from ${allPaths.size} files ")
      files.foreach { case (_, mapByShifts) =>
        log.info(s"--A variation got ${mapByShifts.size} different shifts, for ${mapByShifts.flatMap(_._2).size} files")
        mapByShifts foreach { case (shift, paths) =>
          log.info(s"---The shift $shift got ${paths.size} files : $paths")
        }
      }
      sender ! true
  }

  private def placeNewFile(newSrt: Srt, path: Path): Unit = {
    files.synchronized{
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

}
