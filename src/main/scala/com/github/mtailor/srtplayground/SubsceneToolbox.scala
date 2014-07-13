package com.github.mtailor.srtplayground

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.collection.JavaConverters._

trait SubsceneToolbox {

  def extractLinks(bodyOfMediaPage: String): Iterable[Element] =
    Jsoup
      .parse(bodyOfMediaPage)
      .select(".subtitles table td.a1 a")
      .asScala

  def extractDownloadLink(bodyOfSubtitlePage: String): String =
    Jsoup
      .parse(bodyOfSubtitlePage)
      .select(".download a")
      .attr("href")
  
  def extractId(link: Element): String =
    link
      .attr("href")
      .replaceAll("""^.*/(\d{6})$""", "$1")

  def extractUrl(link: Element): String =
    link
      .attr("href")

  def extractName(link: Element): String =
    link
      .select("span:last-child")
      .text()
      .trim()

  def matchSeasonAndEpisode(name: String, season: Int, episode: Int) =
    name
      .toLowerCase
      .contains(f"s$season%02de$episode%02d")



}
