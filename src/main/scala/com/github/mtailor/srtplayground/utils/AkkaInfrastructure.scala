package com.github.mtailor.srtplayground.utils

import akka.actor.ActorSystem

trait AkkaInfrastructure {

  implicit val actorSystem = ActorSystem()
  implicit val executionContext = actorSystem.dispatcher

  def shutdown = actorSystem.shutdown


}
