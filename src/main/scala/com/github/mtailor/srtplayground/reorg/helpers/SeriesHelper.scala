package com.github.mtailor.srtplayground.reorg.helpers

class SeriesHelper {

  def isForSeasonAndEpisode(subtitleName: String, season: Int, episode: Int): Boolean =
    subtitleName
      .toLowerCase
      .matches(f".*s$season%02de$episode%02d[^\\d].+")

}
