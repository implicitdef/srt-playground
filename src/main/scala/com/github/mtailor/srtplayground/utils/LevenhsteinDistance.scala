package com.github.mtailor.srtplayground.utils

import scala.math._

trait LevenhsteinDistance {

  def similarityRate(s1: String, s2: String) =
    1 - (levenhsteinDistance(s1, s2).toFloat / average(s1.length, s2.length))

  private def average(a: Int, b: Int): Float =
    (a + b).toFloat / 2

  //http://oldfashionedsoftware.com/2009/11/19/string-distance-and-refactoring-in-scala/
  private def levenhsteinDistance(s1: String, s2: String): Int = {
    def minimum(i1: Int, i2: Int, i3: Int) = min(min(i1, i2), i3)

    var dist = ( new Array[Int](s1.length + 1),
      new Array[Int](s1.length + 1) )

    for (idx <- 0 to s1.length) dist._2(idx) = idx

    for (jdx <- 1 to s2.length) {
      val (newDist, oldDist) = dist
      newDist(0) = jdx
      for (idx <- 1 to s1.length) {
        newDist(idx) = minimum (
          oldDist(idx) + 1,
          newDist(idx-1) + 1,
          oldDist(idx-1) + (if (s1(idx-1) == s2(jdx-1)) 0 else 1)
        )
      }
      dist = dist.swap
    }

    dist._2(s1.length)
  }

}
