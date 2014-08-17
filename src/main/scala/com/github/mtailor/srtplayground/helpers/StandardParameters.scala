package com.github.mtailor.srtplayground.helpers

import com.github.mtailor.srtplayground.analysis.SrtFullComparisonHelper.FullComparisonParameters
import com.github.mtailor.srtplayground.analysis.SrtsTextualMatchingHelper.TextualMatchingParameters

trait StandardParameters extends  {

  implicit val params = FullComparisonParameters(
    TextualMatchingParameters(
      30,
      2,
      0.85
    ),
    0.85,
    //TODO voir pourquoi ce parametre ne marche pas comme attendu ??
    1000
  )

}
