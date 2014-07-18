srt-playground
==============

stuff with .srt files, work in progress.

So far the main thing is a proof-of-concept of a simple method ('MainProto') to compare multiples .srt files, which are supposed to be for the same media but might come from different sources and be actually quite different (in formatting, in the texts, in the timing). It parses a set of files, computes the "distance" between each of them, and regroups the ones that are very close. That way we are able to identify :

 - when two files for the same media are almost identical (meaning someone changed a few whitespaces, one sentence, or a timing)
 - when two files are quite different (meaning it's a different transcript, from a different source, for the same media)
 - when two files have nothing to do with each other (meaning one of them is probably not for that media, or not in the same language)





