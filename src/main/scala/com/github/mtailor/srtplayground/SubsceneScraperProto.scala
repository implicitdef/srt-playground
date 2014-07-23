package com.github.mtailor.srtplayground

import com.github.mtailor.srtplayground.utils.{FilesToolbox, SubsceneCaller, SubsceneParser}

import scala.concurrent.Future

object SubsceneScraperProto extends App with SubsceneParser with SubsceneCaller with FilesToolbox {

  val mediaUrl = "/subtitles/mad-men-sixth-season"

  val subtitlesUrlsFuture: Future[Iterable[String]] =
    callForString(mediaUrl) map { body =>
      extractLinksOfMediaPage(body) filter { a =>
        isForSeasonAndEpisode(extractTextOfLink(a), 6, 8)
      } map { a =>
        extractUrlOfLink(a)
      }
    }

  val downloadUrlsFuture: Future[Iterable[String]] = subtitlesUrlsFuture flatMap { urls =>
    Future.sequence (
      urls map {
        callForString(_) map {
          extractDownloadUrlOfSubtitlePage(_)
        }
      }
    )
  }



  val allDoneFuture: Future[Unit] = downloadUrlsFuture flatMap {
    case urls => {
      Future.sequence(
        urls map { url =>
          callForBytes(url) map writeToNewFile
        }
      )
    }
  } map (_ => ())

  allDoneFuture foreach { _ =>
    println("all done, stopping the app")
    shutdown
  }







}
