package com.github.mtailor.srtplayground.reorg


object LevenhsteinDistance extends LevenhsteinDistance

trait LevenhsteinDistance extends ((String, String) => Int) {

  def apply(a: String, b: String): Int = {
    //http://oldfashionedsoftware.com/2009/11/19/string-distance-and-refactoring-in-scala/

    def min(nbs: Int*) = nbs.min

    var dist = (
      new Array[Int](a.length + 1),
      new Array[Int](a.length + 1)
    )

    for (i <- 1 to a.length)
      dist._2(i) = i

    for (j <- 1 to b.length) {
      val (newDist, oldDist) = dist
      newDist(0) = j
      for (i <- 1 to a.length) {
        newDist(i) = min(
          oldDist(i) + 1,
          newDist(i - 1) + 1,
          oldDist(i - 1) + (if (a(i - 1) == b(j - 1)) 0 else 1)
        )
      }
      dist = dist.swap
    }

    dist._2(a.length)
  }



}
