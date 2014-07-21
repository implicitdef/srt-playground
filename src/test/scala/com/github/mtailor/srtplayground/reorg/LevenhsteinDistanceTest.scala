package com.github.mtailor.srtplayground.reorg

import org.specs2.mutable.Specification

class LevenhsteinDistanceTest extends Specification {

  val f = LevenhsteinDistance

  "LevenhsteinDistance" should {
    """find that the distance between "kitten" and "sitting" is 3""" in {
      f("kitten", "sitting") mustEqual 3
    }
    """find that the distance of a normal string with itself is 0""" in {
      f("kitten", "kitten") mustEqual 0
    }
    """find that the distance between a normal string and an empty string is the length of the former""" in {
      f("kitten", "") mustEqual "kitten".length
    }
    """find that the distance between an empty string and single letter is 1""" in {
      f("", "a") mustEqual 1
    }
    """find that the distance between an empty string and two spaces is 2""" in {
      f("", "  ") mustEqual 2
    }
    """find that the distance between two empty strings is 0""" in {
      f("", "") mustEqual 0
    }
    """find that the distance between two strings where the variations are on case and special chars""" in {
      f("abcdefg?", "A:bcdêfg!") mustEqual 4
    }
  }


}
