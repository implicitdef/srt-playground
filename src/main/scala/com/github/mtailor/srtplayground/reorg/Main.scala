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
    mainService.fetchSrtFiles(args(0), args(1).toInt, args(2).toInt, args(3))
  } catch {
    case t: Throwable => t.printStackTrace()
  }
}
