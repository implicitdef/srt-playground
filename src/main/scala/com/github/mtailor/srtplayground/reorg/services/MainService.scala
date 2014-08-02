package com.github.mtailor.srtplayground.reorg.services

import com.github.mtailor.srtplayground.reorg.akka.AkkaAware
import com.github.mtailor.srtplayground.reorg.helpers.{SrtToolbox, StringsComparisonHelper, FilesToolbox, BasicClusteringHelper}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future

class MainService(val subsceneScrapingService: SubsceneScrapingService,
                  val basicClusteringHelper: BasicClusteringHelper,
                  val stringsComparisonHelper: StringsComparisonHelper,
                  val srtToolbox : SrtToolbox,
                  val filesToolbox : FilesToolbox
                  ) extends AkkaAware with LazyLogging {

  /**
   * Given :
   * - an url to a tv show on subscene
   * - a season number
   * - an episode number
   * - a empty directory to work in
   *
   * this will download and unzip all corresponding srt files from
   * Subscene, into that folder, renamed into 1.srt, 2.srt, etc.
   * where similar files have been filtered out.
   *
   */
  def fetchSrtFiles(
    url: String,
    season: Int,
    episode: Int,
    dir: String
  ): Future[Unit] = {
    val allSrtDir = f"$dir/all"
    filesToolbox.makeDir(allSrtDir)
    subsceneScrapingService
      .getAndWriteSrtFiles(url, season, episode, allSrtDir)
      .map { _ =>

        val beginnings = filesBeginningsByFilePath(allSrtDir)

        val groups = basicClusteringHelper.group(
          beginnings.keySet,
          (a: String, b: String) => stringsComparisonHelper.similarityRate(beginnings(a), beginnings(b)) >= 0.85
        )

        groups
          .map (_.head)
          .zipWithIndex
          .foreach { case (path, i) =>
            filesToolbox.moveFile(path, f"$dir/$i.srt")
          }

        groups.foreach(g => logger.info (f"==> $g"))

      filesToolbox.deleteDir(allSrtDir)
        shutdownAkka
      }
  }

  private def filesBeginningsByFilePath(allSrtDir: String): Map[String, String] =
    filesToolbox.filesInFolder(allSrtDir)
      .map { f =>
      (
        f.getAbsolutePath,
        srtToolbox.firstChars(srtToolbox.readSrt(f), 1000)
      )
    }
    .toMap



}

