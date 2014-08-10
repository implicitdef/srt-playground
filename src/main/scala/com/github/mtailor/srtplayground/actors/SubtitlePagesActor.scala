package com.github.mtailor.srtplayground.actors

import java.nio.file.Files._
import java.nio.file.Path

import akka.actor.Actor
import akka.pattern.{ask, pipe}
import com.github.mtailor.srtplayground.ActorPaths
import com.github.mtailor.srtplayground.helpers.StandardTimeout
import org.jsoup.Jsoup
import spray.http.HttpResponse

// handle the page on subscene dedicated to a subtitle
// hits it and download the subtitle, before sending it to the newSubtitlesManagerActor
class SubtitlePagesActor extends Actor with StandardTimeout {
  import context._
  override def receive = {
    case subtitlePageRelativeUrl: String => {
      val httpActor = actorSelection(ActorPaths.subsceneHttpCallsActor)
      //ask for the subtitle page
      (httpActor ? subtitlePageRelativeUrl)
        .mapTo[HttpResponse]
        .foreach { response =>
          val downloadUrl = extractDownloadUrlOfSubtitlePage(response.entity.asString)
          //ask for the zip
          (httpActor ? downloadUrl)
            .mapTo[HttpResponse]
            .foreach { response =>
              // write the zip in a file
              val zipPath = createTempFile(null, null)
              write(zipPath, response.entity.data.toByteArray)
              // ask to the unzipper
              val unzipper = actorSelection(ActorPaths.unzipperActor)
              val newSubtitlesManager = actorSelection(ActorPaths.newSubtitlesManagerActor)
              // send to the newSubtitlesManager
              (unzipper ? zipPath)
                .mapTo[Path]
                .pipeToSelection(newSubtitlesManager)
            }
        }
    }
  }

  private def extractDownloadUrlOfSubtitlePage(bodyOfSubtitlePage: String): String =
      Jsoup
        .parse(bodyOfSubtitlePage)
        .select(".download a")
        .first()
        .attr("href")

}
