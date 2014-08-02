package com.github.mtailor.srtplayground.reorg.akka

import akka.actor.ActorSystem

trait AkkaAware {

  implicit val actorSystem = ActorSystem("MyActorSystem")
  implicit val executionContext = actorSystem.dispatcher

  def shutdown = {
    println("Shutting down the actor system")
    actorSystem.shutdown
  }

}
