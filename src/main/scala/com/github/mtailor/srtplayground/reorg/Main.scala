package com.github.mtailor.srtplayground.reorg

import com.github.mtailor.srtplayground.reorg.akka.AkkaAware
import com.github.mtailor.srtplayground.reorg.helpers._
import com.github.mtailor.srtplayground.reorg.services.{SubsceneScrapingService, MainService}
import com.typesafe.scalalogging.LazyLogging


object Main extends App with AkkaAware with LazyLogging {
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
    val dir = args(1)
    mainService.fetchSrtFiles(
      url,
      _ => true,
      dir
    ) onFailure { case t: Throwable => logger.error("Failure", t)}

  } catch {
    case t: Throwable => case t: Throwable => logger.error("Failure", t)
  }
}
