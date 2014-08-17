package com.github.mtailor.srtplayground.actors

import akka.actor.Actor
import akka.pattern.ask
import com.github.mtailor.srtplayground.helpers.{ActorPaths, StandardTimeout}
import org.jsoup.Jsoup
import spray.http.HttpResponse

import scala.collection.Iterable
import scala.collection.JavaConverters._

class MediaPagesActor extends Actor with StandardTimeout {
  import context._
  override def receive = {
    case movieNameInUrl: String =>
      val httpActor = actorSelection(ActorPaths.subsceneHttpCallsActor)
      val subtitlePagesActor = actorSelection(ActorPaths.subtitlePagesActor)
      (httpActor ? s"/subtitles/$movieNameInUrl")
        .mapTo[HttpResponse]
        .foreach { response =>
          // find all links
          extractSubtitleUrlsOfMediaPage(response.entity.asString) match {
            case Seq() => throw new RuntimeException(s"0 subtitle found for $movieNameInUrl")
            case urls => urls.foreach { url =>
              // and send them
              subtitlePagesActor ! url
            }
          }
        }
  }

  private def extractSubtitleUrlsOfMediaPage(bodyOfMediaPage: String): Seq[String] =
    Jsoup
      .parse(bodyOfMediaPage)
      .select(".subtitles table td.a1 a")
      .asScala
      .map (_.attr("href"))
      .toSeq

}
