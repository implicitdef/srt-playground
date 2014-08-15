package com.github.mtailor.srtplayground.analysis

import com.github.mtailor.srtdissector.Vocabulary._
import com.github.mtailor.srtplayground.analysis.SrtsTextualMatchingHelper.TextualMatchingParameters
import org.specs2.mutable.Specification

class SrtsTextualMatchingHelperTest extends Specification {

  implicit val params = TextualMatchingParameters(5, 3, 0.85)
  val func = new SrtsTextualMatchingHelper().computeMatches _
  def newSrt(strings: Seq[String]*): Srt =
    //just put 0 times, they don't matter here
    strings.map(SubtitleBlock(0, 0, _))

  "SrtsTextualMatchingHelper" should {
    "find the specificed matches for slightly related inputs" in {
      val a  = newSrt(
        Seq("aaa", "aaa"),
        Seq("bbb", "bbb", "bbb"),
        Seq("ccc"),
        Seq("ddd"),
        Seq("eee"),
        Seq("fff")
      )
      val b = newSrt(
        Seq("aaabaaa"), //matches with a(0)
        Seq("zzzz"),  //no match
        Seq("xxxx"), //no match
        Seq("bbb", "bbb", "bbb"), //matches with a(1)
        Seq("dddd"), //matches with a(3)
        Seq("eee")
      )
      func(a, b) must beEqualTo(Seq(
        (a(0), b(0)),
        (a(1), b(3)),
        (a(3), b(4))
      ))
    }
    "find no matches for unrelated inputs" in {
      val a = newSrt(
        Seq("aaa", "aaa"),
        Seq("bbb", "bbb", "bbb"),
        Seq("ccc"),
        Seq("ddd"),
        Seq("eee"),
        Seq("fff")
      )
      val b = newSrt(
        Seq("zzzzz"),
        Seq("qqqqq", "rrr", "kkk", "yyyy")
      )
      func(a, b) must beEmpty
    }
    "find exact for identical inputs" in {
      val a = newSrt(
        Seq("aaa", "aaa"),
        Seq("bbb", "bbb", "bbb"),
        Seq("ccc"),
        Seq("ddd"),
        Seq("eee"),
        Seq("fff")
      )
      func(a, a) must beEqualTo(Seq(
        (a(0), a(0)),
        (a(1), a(1)),
        (a(2), a(2)),
        (a(3), a(3)),
        (a(4), a(4))
      ))
    }
  }
}
