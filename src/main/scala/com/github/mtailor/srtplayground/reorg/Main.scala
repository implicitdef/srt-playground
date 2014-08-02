package com.github.mtailor.srtplayground.reorg

import com.github.mtailor.srtplayground.reorg.services.MainService


object Main extends App {
  try
    MainService.fetchSrtFiles(args(0), args(1).toInt, args(2).toInt, args(3))
  catch {
    case t: Throwable => t.printStackTrace()
  }
}
