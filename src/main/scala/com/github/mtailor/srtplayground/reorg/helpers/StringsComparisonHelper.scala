package com.github.mtailor.srtplayground.reorg.helpers

import org.apache.commons.lang3.StringUtils

class StringsComparisonHelper {

  def similarityRate(s1: String, s2: String) =
    StringUtils.getJaroWinklerDistance(s1, s2)

}
