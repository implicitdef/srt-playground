package com.github.mtailor.srtplayground.reorg.akka

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging

trait AkkaAware extends LazyLogging {

  implicit val actorSystem = ActorSystem("MyActorSystem")
  implicit val executionContext = actorSystem.dispatcher

  def shutdownAkka = {
    logger.info("Shutting down the actor system")
    actorSystem.shutdown
  }

}
