package com.github.mtailor.srtplayground.reorg.services

import com.github.mtailor.srtplayground.reorg.akka.AkkaAware
import com.github.mtailor.srtplayground.reorg.helpers.FilesToolbox
import com.typesafe.scalalogging.LazyLogging
import net.lingala.zip4j.core.ZipFile
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import spray.client.pipelining._
import spray.http.HttpHeaders.Cookie
import spray.http.{HttpCookie, HttpRequest, HttpResponse}

import scala.collection.JavaConverters._
import scala.concurrent.Future




class SubsceneScrapingService(val filesToolbox: FilesToolbox) extends AkkaAware with LazyLogging {

  /**
   * Given :
   * - an url to a tv show on subscene
   * - a season number
   * - an episode number
   * - an empty temporary directory to work in
   *
   * this will download  and unzip all corresponding srt files
   * from Subscene, into the final directory, renamed
   * as 1.srt, 2.srt, etc.
   *
   */
  def getAndWriteSrtFiles(
    url: String,
    season: Int,
    episode: Int,
    dir: String
  ): Future[Unit] = {

    val subtitlesUrlsFuture: Future[Iterable[String]] =
      callForString(url) map { body =>
        extractLinksOfMediaPage(body) filter { a =>
          isForSeasonAndEpisode(extractTextOfLink(a), season, episode)
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
        filesToolbox.makeDir(f"$dir/zips")
        filesToolbox.makeDir(f"$dir/unzips")
        Future.sequence(
          urls
            .zipWithIndex
            .map { case (url, i) =>
              callForBytes(url)
                .map { bytes =>
                  val zipPath = f"$dir/zips/$i.zip"
                  val unzippedPath = f"$dir/unzips/$i"
                filesToolbox.writeToNewFile(bytes, zipPath)
                  new ZipFile(zipPath).extractAll(unzippedPath)
                  if (filesToolbox.containsOneFile(unzippedPath)) {
                    filesToolbox.moveFile(filesToolbox.filesInFolder(unzippedPath).head.getAbsolutePath, f"$dir/$i.srt")
                  }
                }
            }
        )
      }
    } map { _ =>
      filesToolbox.deleteDir(f"$dir/zips")
      filesToolbox.deleteDir(f"$dir/unzips")
    }

    allDoneFuture

  }



  private val domain = "http://subscene.com"

  private def callForString(relativeUrl: String): Future[String] =
  call(relativeUrl) map (_.entity.asString)

  private def callForBytes(relativeUrl: String): Future[Array[Byte]] =
  call(relativeUrl) map (_.entity.data.toByteArray)

  private def call(relativeUrl: String): Future[HttpResponse] = {
    val url = domain + relativeUrl
    logger.info(f"Requesting $url")
    pipeline(Get(url)) andThen {
      case _ => logger.info(f"OK $url")
    }
  }

  private val pipeline: HttpRequest => Future[HttpResponse] =
    addHeader(Cookie(HttpCookie("LanguageFilter", "13")))
      .andThen(sendReceive)

  private def extractLinksOfMediaPage(bodyOfMediaPage: String): Iterable[Element] =
    Jsoup
      .parse(bodyOfMediaPage)
      .select(".subtitles table td.a1 a")
      .asScala

  private def extractDownloadUrlOfSubtitlePage(bodyOfSubtitlePage: String): String =
    extractUrlOfLink(
      Jsoup
        .parse(bodyOfSubtitlePage)
        .select(".download a")
        .first()
    )

  private def extractIdOfLinkToSubtitle(link: Element): String =
    extractUrlOfLink(link)
      .replaceAll("""^.*/(\d{6})$""", "$1")

  private def extractUrlOfLink(link: Element): String =
    link
      .attr("href")

  private def extractTextOfLink(link: Element): String =
    link
      .select("span:last-child")
      .text()
      .trim()

  private def isForSeasonAndEpisode(name: String, season: Int, episode: Int) =
    name
      .toLowerCase
      .contains(f"s$season%02de$episode%02d")



}