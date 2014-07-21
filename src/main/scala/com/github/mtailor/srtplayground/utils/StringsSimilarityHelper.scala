package com.github.mtailor.srtplayground.utils

import com.github.mtailor.srtplayground.reorg.LevenhsteinDistance

trait StringsSimilarityHelper  {

  def similarityRate(s1: String, s2: String) =
    1 - (LevenhsteinDistance(s1, s2).toFloat / average(s1.length, s2.length))

  private def average(a: Int, b: Int): Float =
    (a + b).toFloat / 2



}
