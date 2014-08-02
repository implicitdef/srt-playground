package com.github.mtailor.srtplayground.reorg.helpers

import org.apache.commons.lang3.StringUtils

trait StringsApproximateComparison extends ((String, String) => Double)

object StringsApproximateComparison extends StringsApproximateComparison {

  override def apply(s1: String, s2: String) =
    StringUtils.getJaroWinklerDistance(s1, s2)

}
