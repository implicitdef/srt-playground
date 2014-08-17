package com.github.mtailor.srtplayground.helpers

import java.io.File
import java.nio.file.Path

import akka.actor.{ActorLogging, Actor}
import akka.util.Timeout
import com.github.mtailor.srtplayground.analysis.SrtFullComparisonHelper.FullComparisonParameters
import com.github.mtailor.srtplayground.analysis.SrtsTextualMatchingHelper.TextualMatchingParameters
import scala.concurrent.duration._

object BaseActor {

  val comparisonParameters = FullComparisonParameters(
    TextualMatchingParameters(
      30,
      2,
      0.85
    ),
    0.85,
    1000
  )

}

abstract class BaseActor extends Actor with ActorLogging {

  implicit val executionContext = context.system.dispatcher

  implicit val comparisonParameters = BaseActor.comparisonParameters

  implicit val standardTimeout: Timeout = 60.seconds

  implicit def path2File(p: Path): File =
    p.toFile

  implicit def file2Path(f: File): Path =
    f.toPath

  val tempFilesPrefix = "SRT"

  private def get(s: String) = context.actorSelection(s)

  val filesDeletionActor       = get("/user/filesDeletionActor")
  val subsceneHttpCallsActor   = get("/user/subsceneHttpCallsActor")
  val unzipperActor            = get("/user/unzipperActor")
  val parsingActor             = get("/user/parsingActor")
  val mediaPageActor           = get("/user/mediaPageActor")
  val subtitlePagesActor       = get("/user/subtitlePagesActor")
  val subtitlesClassifierActor = get("/user/subtitlesClassifierActor")
  val monitoringActor          = get("/user/monitoringActor")

}
