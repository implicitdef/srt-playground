package com.github.mtailor.srtplayground

import akka.actor.{Props, ActorSystem}
import com.github.mtailor.srtplayground.actors._
import com.github.mtailor.srtplayground.helpers.{StringsComparisonHelper, SrtHelper, BasicClusteringHelper}

import scala.concurrent.duration._

object Main extends App {

  val system = ActorSystem("SrtPlayground")
  implicit val executionContext = system.dispatcher

  system.actorOf(Props[FilesDeletionActor], name = "filesDeletionActor")
  system.actorOf(Props[UnzipperActor], name = "unzipperActor")
  system.actorOf(Props[SubsceneHttpCallsActor], name = "subsceneHttpCallsActor")

  val mediaPagesActor = system.actorOf(Props[MediaPagesActor], name = "mediaPagesActor")
  system.actorOf(Props[SubtitlePagesActor], name = "subtitlePagesActor")
  val newSubtitlesManagerActor = system.actorOf(
    Props(classOf[NewSubtitlesManagerActor],
      new BasicClusteringHelper,
      new SrtHelper,
      new StringsComparisonHelper
    ), name = "newSubtitlesManagerActor")

  mediaPagesActor ! "captain-america-the-winter-soldier"

  system.scheduler.schedule(5.seconds, 10.seconds, newSubtitlesManagerActor, SubtitlesAnalysisSignal)

  //TODO améliorer la suppression de fichiers pour qu'un maximum de choses soit supprimées
}
