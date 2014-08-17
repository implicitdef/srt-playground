package com.github.mtailor.srtplayground.actors

import java.nio.file.Path

import com.github.mtailor.srtplayground.helpers.BaseActor
import org.apache.commons.io.FileUtils


class FilesDeletionActor extends BaseActor {

  override def receive = {
    case toDelete: Path => {
      log.info(s"Deleting $toDelete")
      FileUtils.forceDelete(toDelete)
    }
  }


}
