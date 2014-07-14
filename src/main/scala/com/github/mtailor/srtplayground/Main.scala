package com.github.mtailor.srtplayground

import java.io.{File, FileOutputStream}

import akka.actor.ActorSystem
import spray.client.pipelining._
import spray.http.HttpEntity.NonEmpty
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
    pipeline(Get(url)) andThen {
      case _ =>  println(f"OK $url")
    }
  }

  def hitForString(url: String): Future[String] =
    hit(url) map (_.entity.asString)

  def hitForBytes(url: String) =
    hit(url) map (_.entity match {
      case NonEmpty(contentType, data) => data.toByteArray
      case _ => throw new RuntimeException("The body was empty")
    })

    def writeToFile(bytes: Array[Byte], filePath: String) = {
      println(f"writing into file $filePath")
      val fos = new FileOutputStream(new File(filePath))
      try {
        fos.write(bytes)
      } finally {
        fos.close()
      }
    }

  def filePathForId(id: String) =
    "/home/manu/dev/writes/" + id + ".srt"



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


  var cpt = 0


  futureDownloadLinks map {
    case links => {
      links map { link =>
        hitForBytes(url) foreach { bytes =>
         cpt += 1
         writeToFile(bytes, filePathForId(cpt.toString))
        }
      }
    }
  }







}
