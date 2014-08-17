package com.github.mtailor.srtplayground.actors

import com.github.mtailor.srtplayground.helpers.BaseActor
import spray.client.pipelining._
import spray.http.HttpHeaders.Cookie
import spray.http.{HttpCookie, HttpRequest, HttpResponse}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class SubsceneHttpCallsActor extends BaseActor {

  implicit val actorRefFactory = context

  private val call: HttpRequest => Future[HttpResponse] =
    addHeader(Cookie(HttpCookie("LanguageFilter", "13")))
      .andThen(sendReceive)

  override def receive = {
    case relativeUrl: String =>
      val originalSender = sender
      val url = s"http://subscene.com$relativeUrl"
      log.info(s">>> $url")
      call(Get(url)) onComplete {
        case Success(response) =>
          originalSender ! response
        case Failure(t) => throw t
      }
  }


}
