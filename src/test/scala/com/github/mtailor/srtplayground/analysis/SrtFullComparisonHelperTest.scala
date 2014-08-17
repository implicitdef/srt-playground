package com.github.mtailor.srtplayground.analysis

import java.io.File

import com.github.mtailor.srtplayground.analysis.SrtFullComparisonHelper._
import com.github.mtailor.srtplayground.helpers.{BaseActor, SrtHelper}
import org.specs2.mutable.Specification

class SrtFullComparisonHelperTest extends Specification {

  implicit val params = BaseActor.comparisonParameters
  def compare(base: String, other: String) = {
    def parse(s: String) =
      new SrtHelper().readSrt(new File(getClass.getClassLoader.getResource(s).toURI)).get
    new SrtFullComparisonHelper(new SrtsTextualMatchingHelper).compare(parse(base), parse(other))
  }


  // we test several classes together :
  // the SrtFullComparisonHelper
  // but also the SrtsTextualMatchingHelper
  // and the various parameters

  "SrtFullComparisonHelper" should {
    "find that two subtitles of two different medias are Unrelated" in {
      compare("avengers.srt", "captain_america_winter_soldier.srt") must beEqualTo(Unrelated)
    }
    "find that two different transcriptions for the same media are Unrelated" in {
      compare("got_s01e01_a.srt", "got_s01e01_b.srt") must beEqualTo(Unrelated)
    }
    "find that two subtitles with same transcription but really unrelated timings are SameTextUnrelatedTimings" in {
      compare("got_s01e01_b.srt", "got_s01e01_d.srt") must beEqualTo(SameTextUnrelatedTimings)
    }
    "find that two subtitles with same transcription and shift in timings are SameTextShiftedTimings" in {
      compare("got_s01e01_b.srt", "got_s01e01_c.srt") must beEqualTo(SameTextShiftedTimings(TimingShift(322,0.9995184992183429)))
    }
    "find that two identical subtitles are SameTextShiftedTimings with a 0 shift" in {
      compare("avengers.srt", "avengers.srt") must beEqualTo(SameTextShiftedTimings(ZeroShift))
    }


  }


}
