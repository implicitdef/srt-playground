package com.github.mtailor.srtplayground.helpers

import akka.util.Timeout
import scala.concurrent.duration._

trait StandardTimeout {

  implicit val standardTimeout: Timeout = 60.seconds

}
