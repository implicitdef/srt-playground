package com.github.mtailor.srtplayground.actors

import java.nio.file.{Files, Path}
import com.github.mtailor.srtplayground.helpers.PathConversions._

import akka.actor.{ActorLogging, Actor}
import org.apache.commons.io.FileUtils


class FilesDeletionActor extends Actor with ActorLogging {

  override def receive = {
    case toDelete: Path => {
      log.info(s"Deleting $toDelete")
      FileUtils.forceDelete(toDelete)
    }
  }


}
