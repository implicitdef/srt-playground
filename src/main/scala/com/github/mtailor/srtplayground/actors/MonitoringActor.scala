package com.github.mtailor.srtplayground.actors

import akka.actor.ReceiveTimeout
import com.github.mtailor.srtplayground.actors.MonitoringActor.{SubtitleProcessFailure, SubtitleProcessSuccess, DownloadsStarted}
import com.github.mtailor.srtplayground.actors.SubtitlesClassifierActor.SubtitlesDumpingSignal
import com.github.mtailor.srtplayground.helpers.BaseActor
import scala.concurrent.duration._
import akka.pattern.ask

object MonitoringActor {

  case class DownloadsStarted(numberOfSubtitles: Int)
  object SubtitleProcessSuccess
  object SubtitleProcessFailure

}

class MonitoringActor extends BaseActor {

  var cptSuccesses, cptFailures, expected = 0
  override def receive = {
    case DownloadsStarted(numberOfSubtitles) =>
      expected = numberOfSubtitles
      context.setReceiveTimeout(10.seconds)
      context.become {
        case SubtitleProcessSuccess =>
          cptSuccesses += 1
          checkCounts()
        case SubtitleProcessFailure =>
          cptFailures += 1
          checkCounts()
        case ReceiveTimeout =>
          log.warning("Timeout, will finish the program ")
          endIt()
      }
  }

  private def checkCounts() = {
    log.info(s"Processed $cptSuccesses, failed $cptFailures, out of $expected")
    if (cptSuccesses + cptFailures >= expected) {
      endIt()
    }
  }

  private def endIt() =
    subtitlesClassifierActor ? SubtitlesDumpingSignal onComplete {_ =>
      log.info("shutting down the actor system")
      context.system.shutdown()
    }
}
