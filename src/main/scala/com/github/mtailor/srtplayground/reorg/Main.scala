package com.github.mtailor.srtplayground.reorg

import com.github.mtailor.srtplayground.reorg.helpers.{FilesHelper, SrtHelper, StringsComparisonHelper, BasicClusteringHelper}
import com.github.mtailor.srtplayground.reorg.services.{SubsceneScrapingService, MainService}


object Main extends App {
  try {
    val filesHelper = new FilesHelper
    val mainService = new MainService(
      new SubsceneScrapingService(filesHelper),
      new BasicClusteringHelper,
      new StringsComparisonHelper,
      new SrtHelper,
      filesHelper
    )

    val url = args(0)
    val season = args(1).toInt
    val nbEpisodes = args(2).toInt
    val dir = args(3)

    1 to nbEpisodes foreach { ep =>
      mainService.fetchSrtFiles(url, season, ep, args(3) + "/" + ep)
    }
  } catch {
    case t: Throwable => t.printStackTrace()
  }
}
