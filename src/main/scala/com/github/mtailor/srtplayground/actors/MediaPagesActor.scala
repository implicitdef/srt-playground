package com.github.mtailor.srtplayground.actors

import akka.pattern.ask
import com.github.mtailor.srtplayground.actors.MonitoringActor.DownloadsStarted
import com.github.mtailor.srtplayground.helpers.BaseActor
import org.jsoup.Jsoup
import spray.http.HttpResponse

import scala.collection.JavaConverters._

class MediaPagesActor extends BaseActor {
  override def receive = {
    case movieNameInUrl: String =>
      (subsceneHttpCallsActor ? s"/subtitles/$movieNameInUrl")
        .mapTo[HttpResponse]
        .foreach { response =>
          // find all links
          extractSubtitleUrlsOfMediaPage(response.entity.asString) match {
            case Seq() => throw new RuntimeException(s"0 subtitle found for $movieNameInUrl")
            case urls =>
              monitoringActor ! DownloadsStarted(urls.size)
              urls.foreach { url =>
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
