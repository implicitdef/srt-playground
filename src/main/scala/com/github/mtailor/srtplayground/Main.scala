package com.github.mtailor.srtplayground

import akka.actor.ActorSystem
import spray.client.pipelining._
import spray.http.HttpHeaders.Cookie
import spray.http._

import scala.concurrent.Future
import scala.concurrent.duration._

object Main extends App with SubsceneToolbox {

  implicit val system = ActorSystem()
  implicit val timeout = 60.seconds

  import com.github.mtailor.srtplayground.Main.system.dispatcher

  // execution context for futures

  val pipeline: HttpRequest => Future[HttpResponse] =
    addHeader(Cookie(HttpCookie("LanguageFilter", "13")))
      .andThen(sendReceive)

  def hit(url: String): Future[HttpResponse] = {
    println(f"Requesting $url")
    pipeline(Get(url)) map { res =>
      println(f"OK $url")
      res
    }
  }


  val url = "http://subscene.com/subtitles/game-of-thrones-first-season"

  val urlsFuture: Future[Iterable[String]] =
    hit(url) map {
      _.entity.asString
    } map { s =>
      extractLinks(s) filter { a =>
        matchSeasonAndEpisode(extractName(a), 1, 1)
      } map { a =>
        "http://subscene.com"  + extractUrl(a)
      }
    }



  val futureDownloadLinks: Future[Iterable[String]] = urlsFuture flatMap { urls =>
    Future.sequence (
      urls map {
        hit(_) map {
          _.entity.asString
        } map {
          "http://subscene.com"  + extractDownloadLink(_)
        }
      }
    )
  }



  futureDownloadLinks andThen {
    case links => {
      links foreach println
      system.shutdown()
    }
  }





}
