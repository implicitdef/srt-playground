package com.github.mtailor.srtplayground.helpers

import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.lang3.StringUtils

class StringsComparisonHelper extends LazyLogging {

  def similarityRate(s1: String, s2: String) = {
    StringUtils.getJaroWinklerDistance(s1, s2)
  }

}
