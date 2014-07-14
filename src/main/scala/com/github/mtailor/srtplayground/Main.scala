package com.github.mtailor.srtplayground

import scala.concurrent.Future

object Main extends App with SubsceneParser with SubsceneCaller with FilesToolbox {

  val mediaUrl = "/subtitles/game-of-thrones-first-season"

  val subtitlesUrlsFuture: Future[Iterable[String]] =
    callForString(mediaUrl) map { body =>
      extractLinksOfMediaPage(body) filter { a =>
        isForSeasonAndEpisode(extractTextOfLink(a), 1, 1)
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

  val allDoneFuture: Future[Unit] = downloadUrlsFuture map {
    case urls => {
      Future.sequence(
        urls map { url =>
          callForBytes(url) map { bytes =>
            writeToNewFile(bytes)
          }
        }
      )
    }
  }

  allDoneFuture foreach { _ =>
    println("all done, stopping the app")
    shutdown
  }







}
