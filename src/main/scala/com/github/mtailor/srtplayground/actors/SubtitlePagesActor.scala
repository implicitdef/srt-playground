package com.github.mtailor.srtplayground.actors

import java.nio.file.Files._
import java.nio.file.Path

import akka.pattern.ask
import com.github.mtailor.srtplayground.helpers.BaseActor
import org.jsoup.Jsoup
import spray.http.HttpResponse

// handle the page on subscene dedicated to a subtitle
// hits it and download the subtitle, before sending it to the newSubtitlesManagerActor
class SubtitlePagesActor extends BaseActor {
  override def receive = {
    case subtitlePageRelativeUrl: String => {
      //ask for the subtitle page
      (subsceneHttpCallsActor ? subtitlePageRelativeUrl)
        .mapTo[HttpResponse]
        .foreach { response =>
          val downloadUrl = extractDownloadUrlOfSubtitlePage(response.entity.asString)
          //ask for the zip
          (subsceneHttpCallsActor ? downloadUrl)
            .mapTo[HttpResponse]
            .foreach { response =>
              // write the zip in a file
              val zipPath = createTempFile(tempFilesPrefix, tempFilesPrefix)
              write(zipPath, response.entity.data.toByteArray)
              // ask to the unzipper
              val unzippedPathOptionFuture = (unzipperActor ? zipPath).mapTo[Option[Path]]
              //the zip will need to be removed
              unzippedPathOptionFuture onComplete {
                case _ => filesDeletionActor ! zipPath
              }
              // send to the newSubtitlesManager, if the unzip is a success
              for {
                option <- unzippedPathOptionFuture
                unzippedPath <- option
              } subtitlesClassifierActor ! unzippedPath
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
