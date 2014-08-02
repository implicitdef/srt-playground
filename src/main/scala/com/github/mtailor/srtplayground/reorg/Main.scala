package com.github.mtailor.srtplayground.reorg

import com.github.mtailor.srtplayground.reorg.helpers.{FilesToolbox, SrtToolbox, StringsComparisonHelper, BasicClusteringHelper}
import com.github.mtailor.srtplayground.reorg.services.{SubsceneScrapingService, MainService}


object Main extends App {
  try {
    val filesToolbox = new FilesToolbox
    val mainService = new MainService(
      new SubsceneScrapingService(filesToolbox),
      new BasicClusteringHelper,
      new StringsComparisonHelper,
      new SrtToolbox,
      filesToolbox
    )
    mainService.fetchSrtFiles(args(0), args(1).toInt, args(2).toInt, args(3))
  } catch {
    case t: Throwable => t.printStackTrace()
  }
}
