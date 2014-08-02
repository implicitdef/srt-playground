package com.github.mtailor.srtplayground.reorg.services

import com.github.mtailor.srtplayground.reorg.akka.AkkaAware
import com.github.mtailor.srtplayground.reorg.helpers.{BasicClustering, FilesToolbox, SrtToolbox, StringsApproximateComparison}

import scala.concurrent.Future

trait MainService {

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
  ): Future[Unit]

}

object MainService extends MainService with AkkaAware {

  override def fetchSrtFiles(
    url: String,
    season: Int,
    episode: Int,
    dir: String
  ): Future[Unit] = {
    val allSrtDir = f"$dir/all"
    FilesToolbox.makeDir(allSrtDir)
    SubsceneScrapingService
      .getAndWriteSrtFiles(url, season, episode, allSrtDir)
      .map { _ =>

        val beginnings = filesBeginningsByFilePath(allSrtDir)

        val groups = BasicClustering.group(
          beginnings.keySet,
          (a: String, b: String) => StringsApproximateComparison(beginnings(a), beginnings(b)) >= 0.85
        )

        groups
          .map (_.head)
          .zipWithIndex
          .foreach { case (path, i) =>
            FilesToolbox.moveFile(path, f"$dir/$i.srt")
          }

        groups.foreach(g => println (f"==> $g"))

        FilesToolbox.deleteDir(allSrtDir)
        shutdown
      }
  }

  private def filesBeginningsByFilePath(allSrtDir: String): Map[String, String] =
    FilesToolbox.filesInFolder(allSrtDir)
      .map { f =>
      (
        f.getAbsolutePath,
        SrtToolbox.firstChars(SrtToolbox.readSrt(f), 1000)
      )
    }
    .toMap



}

