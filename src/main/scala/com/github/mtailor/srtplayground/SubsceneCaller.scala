package com.github.mtailor.srtplayground

import spray.client.pipelining._
import spray.http.HttpHeaders.Cookie
import spray.http.{HttpCookie, HttpRequest, HttpResponse}

import scala.concurrent.Future

trait SubsceneCaller extends AkkaInfrastructure {


  def callForString(relativeUrl: String): Future[String] =
    call(relativeUrl) map (_.entity.asString)

  def callForBytes(relativeUrl: String): Future[Array[Byte]] =
    call(relativeUrl) map (_.entity.data.toByteArray)

  private val domain = "http://subscene.com"

  private def call(relativeUrl: String): Future[HttpResponse] = {
    val url = domain + relativeUrl
    println(f"Requesting $url")
    pipeline(Get(url)) andThen {
      case _ =>  println(f"OK $url")
    }
  }

  private val pipeline: HttpRequest => Future[HttpResponse] =
    addHeader(Cookie(HttpCookie("LanguageFilter", "13")))
      .andThen(sendReceive)


}
