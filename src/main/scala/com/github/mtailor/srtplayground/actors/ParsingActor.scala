package com.github.mtailor.srtplayground.actors

import java.nio.file.Path
import com.github.mtailor.srtplayground.actors.MonitoringActor.SubtitleProcessFailure

import scala.util._
import com.github.mtailor.srtplayground.helpers.{BaseActor, SrtHelper}

class ParsingActor(srtHelper: SrtHelper) extends BaseActor {

  override def receive = {
    case p: Path => {
      srtHelper.readSrt(p) match {
        case Success(srt) =>
          sender ! srt
        case Failure(t) =>
          log.warning(s"Failed to parse $p")
          monitoringActor ! SubtitleProcessFailure
      }
    }
  }

}
