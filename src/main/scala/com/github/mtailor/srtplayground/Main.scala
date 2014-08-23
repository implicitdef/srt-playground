package com.github.mtailor.srtplayground

import akka.actor.{ActorSystem, Props}
import com.github.mtailor.srtplayground.actors._
import com.github.mtailor.srtplayground.analysis.{SrtFullComparisonHelper, SrtsTextualMatchingHelper}
import com.github.mtailor.srtplayground.helpers.SrtHelper

object Main extends App {

  val system = ActorSystem("SrtPlayground")

  system.actorOf(Props[FilesDeletionActor], name = "filesDeletionActor")
  system.actorOf(Props[UnzipperActor], name = "unzipperActor")
  system.actorOf(Props[SubsceneHttpCallsActor], name = "subsceneHttpCallsActor")

  val mediaPagesActor = system.actorOf(Props[MediaPagesActor], name = "mediaPagesActor")
  system.actorOf(Props[SubtitlePagesActor], name = "subtitlePagesActor")
  system.actorOf(Props[MonitoringActor], name = "monitoringActor")
  system.actorOf(
    Props(classOf[ParsingActor],
    new SrtHelper
  ), name = "parsingActor")
  val newSubtitlesManagerActor = system.actorOf(
    Props(classOf[SubtitlesClassifierActor],
      new SrtFullComparisonHelper(
        new SrtsTextualMatchingHelper
      )
    ), name = "subtitlesClassifierActor")

  //TODO rajouter support des séries, avec filtres sur l'episode
  mediaPagesActor ! "godzilla"

}
