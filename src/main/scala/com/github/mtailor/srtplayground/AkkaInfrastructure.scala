package com.github.mtailor.srtplayground

import akka.actor.ActorSystem

trait AkkaInfrastructure {

  implicit val actorSystem = ActorSystem()
  implicit val executionContext = actorSystem.dispatcher

  def shutdown = actorSystem.shutdown


}
