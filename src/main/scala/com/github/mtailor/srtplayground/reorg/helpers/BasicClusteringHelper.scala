package com.github.mtailor.srtplayground.reorg.helpers


class BasicClusteringHelper {

  /**
   * Regroups a bunch of values into subgroups, given
   * a function that tell us, for two values, if
   * they should be in the same group.
   *
   * Note : the values are merged aggressively.
   * If shouldBeRegrouped(a, b) == true
   * and shouldBeRegrouped(b, c) == true
   * then (a, b, c) WILL be regrouped
   * regardless of shouldBeRegrouped(a, c)
   */
  def group[A](
    values: Set[A],
    shouldBeRegrouped: (A, A) => Boolean
  ): Set[Set[A]] = {

    val initialClusters: Set[Set[A]] = values map (Set(_))
    val couplesToMerge: Set[(A, A)] =
      values
        .toSeq
        .combinations(2)
        .map { case Seq(a, b) => (a, b)}
        .filter { case (a, b) => shouldBeRegrouped(a, b)}
        .toSet
    couplesToMerge.foldLeft(initialClusters)(mergeSubSets)

  }

  // Merge the two subsets that contains
  // the two parts of the tuple, if they are not already
  // in the same subset
  private def mergeSubSets[A](setOfSets: Set[Set[A]], coupleToMerge: (A, A)): Set[Set[A]] = {
    val (a, b) = coupleToMerge
    val subsetA = findSubset(setOfSets, a)
    val subsetB = findSubset(setOfSets, b)
    if(subsetA == subsetB)
      setOfSets
    else
      setOfSets + (subsetA ++ subsetB) - subsetA - subsetB
  }

  //Find the subset that contains a
  private def findSubset[A](setOfSets: Set[Set[A]], a: A): Set[A] =
    setOfSets find (_.contains(a)) getOrElse (
      throw new RuntimeException(f"the value $a was not contained in any subset of $setOfSets")
    )


}
