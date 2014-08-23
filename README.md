srt-playground
==============

Pitch
-----

Given some movie name, the goal is to be able to present all different versions (i.e. different texts or different timings) of subtitles existing on the web.

Current state
-----

For now it's just a jar that outputs the results on the console. But it works.
Sample output :

    [INFO] >>> http://subscene.com/subtitles/godzilla
    [INFO] >>> http://subscene.com/subtitles/godzilla/english/968716
    [INFO] >>> http://subscene.com/subtitles/godzilla/english/968590
    [INFO] >>> http://subscene.com/subtitles/godzilla/english/968542
    [INFO] >>> http://subscene.com/subtitles/godzilla/english/968542
    (...)
    [INFO] Processed 164, failed 3, out of 170
    [INFO] Processed 165, failed 3, out of 170
    [INFO] Processed 166, failed 3, out of 170
    [INFO] Processed 167, failed 3, out of 170
    [INFO] ----------------------
    [INFO] -Found 16 textual variations from 167 files
    [INFO] --A variation got 1 different shifts, for 1 files
    [INFO] ---The shift TimingShift(0,1.0) got 1 files : ListBuffer(/tmp/SRT6695783586265042647SRT)
    [INFO] --A variation got 1 different shifts, for 9 files
    [INFO] ---The shift TimingShift(0,1.0) got 9 files : ListBuffer(/tmp/SRT3625311665846403179SRT, ...
    [INFO] --A variation got 1 different shifts, for 2 files
    [INFO] ---The shift TimingShift(0,1.0) got 2 files : ListBuffer(/tmp/SRT4017774021566508634SRT, ...
    [INFO] --A variation got 1 different shifts, for 1 files
    [INFO] ---The shift TimingShift(0,1.0) got 1 files : ListBuffer(/tmp/SRT3098561118953106341SRT)
    [INFO] --A variation got 1 different shifts, for 5 files
    [INFO] ---The shift TimingShift(0,1.0) got 5 files : ListBuffer(/tmp/SRT7569062178598663855SRT, ...
    [INFO] --A variation got 1 different shifts, for 1 files
    [INFO] ---The shift TimingShift(0,1.0) got 1 files : ListBuffer(/tmp/SRT3161460222039310875SRT)
    [INFO] --A variation got 1 different shifts, for 1 files
    [INFO] ---The shift TimingShift(0,1.0) got 1 files : ListBuffer(/tmp/SRT2768169125216683551SRT)
    [INFO] --A variation got 1 different shifts, for 1 files
    [INFO] ---The shift TimingShift(0,1.0) got 1 files : ListBuffer(/tmp/SRT5417581507988880159SRT)
    [INFO] --A variation got 1 different shifts, for 21 files
    [INFO] ---The shift TimingShift(0,1.0) got 21 files : ListBuffer(/tmp/SRT3042626538952485293SRT, ...
    [INFO] --A variation got 1 different shifts, for 50 files
    [INFO] ---The shift TimingShift(0,1.0) got 50 files : ListBuffer(/tmp/SRT8976341201613925009SRT, ...
    [INFO] --A variation got 4 different shifts, for 10 files
    [INFO] ---The shift TimingShift(23292,1.0004701176148796) got 1 files : ListBuffer(/tmp/SRT1918148559308889725SRT)
    [INFO] ---The shift TimingShift(-1000,1.0) got 1 files : ListBuffer(/tmp/SRT920634744919508990SRT)
    [INFO] ---The shift TimingShift(0,1.0) got 1 files : ListBuffer(/tmp/SRT5235449940409295957SRT)
    [INFO] ---The shift TimingShift(39000,1.0) got 7 files : ListBuffer(/tmp/SRT3282507320814921066SRT, ...
    [INFO] --A variation got 1 different shifts, for 3 files
    [INFO] ---The shift TimingShift(0,1.0) got 3 files : ListBuffer(/tmp/SRT8144893917351306548SRT, ...
    [INFO] --A variation got 1 different shifts, for 18 files
    [INFO] ---The shift TimingShift(0,1.0) got 18 files : ListBuffer(/tmp/SRT7976587064470281979SRT, ...
    [INFO] --A variation got 1 different shifts, for 3 files
    [INFO] ---The shift TimingShift(0,1.0) got 3 files : ListBuffer(/tmp/SRT3000276441462859218SRT, ...
    [INFO] --A variation got 1 different shifts, for 1 files
    [INFO] ---The shift TimingShift(0,1.0) got 1 files : ListBuffer(/tmp/SRT4484307580429951015SRT)
    [INFO] --A variation got 1 different shifts, for 40 files
    [INFO] ---The shift TimingShift(0,1.0) got 40 files : ListBuffer(/tmp/SRT1581474423297195850SRT, ...









