package com.github.mtailor.srtplayground

import akka.actor.{ActorSystem, Props}
import com.github.mtailor.srtplayground.actors.NewSubtitlesManagerActor.SubtitlesDumpingSignal
import com.github.mtailor.srtplayground.actors._
import com.github.mtailor.srtplayground.analysis.{SrtFullComparisonHelper, SrtsTextualMatchingHelper}
import com.github.mtailor.srtplayground.helpers.{BasicClusteringHelper, SrtHelper}

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
      new SrtFullComparisonHelper(
        new SrtsTextualMatchingHelper
      )
    ), name = "newSubtitlesManagerActor")

  mediaPagesActor ! "captain-america-the-winter-soldier"

  system.scheduler.schedule(5.seconds, 10.seconds, newSubtitlesManagerActor, SubtitlesDumpingSignal)

}
