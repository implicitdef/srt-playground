package com.github.mtailor.srtplayground

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.collection.JavaConverters._

trait SubsceneParser {

  def extractLinksOfMediaPage(bodyOfMediaPage: String): Iterable[Element] =
    Jsoup
      .parse(bodyOfMediaPage)
      .select(".subtitles table td.a1 a")
      .asScala

  def extractDownloadUrlOfSubtitlePage(bodyOfSubtitlePage: String): String =
    extractUrlOfLink(
      Jsoup
      .parse(bodyOfSubtitlePage)
      .select(".download a")
      .first()
    )

  def extractIdOfLinkToSubtitle(link: Element): String =
    extractUrlOfLink(link)
      .replaceAll("""^.*/(\d{6})$""", "$1")

  def extractUrlOfLink(link: Element): String =
    link
      .attr("href")

  def extractTextOfLink(link: Element): String =
    link
      .select("span:last-child")
      .text()
      .trim()

  def isForSeasonAndEpisode(name: String, season: Int, episode: Int) =
    name
      .toLowerCase
      .contains(f"s$season%02de$episode%02d")



}
