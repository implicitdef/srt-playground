package com.github.mtailor.srtplayground.utils

trait ClusteringAlgorithm {

  /**
   * Regroups a bunch of values into clusters, given
   * a criterion function that tells us if two values
   * should be in the same cluster
   *
   */
  def computeClusters[A](values: Set[A], clustersCriterion: (A, A) => Boolean): Set[Set[A]] = {
    val initialClusters: Set[Set[A]] = values map (Set(_))
    val couplesToMerge: Set[(A, A)] =
      values
        .toSeq
        .combinations(2)
        .map { case Seq(a, b) => (a, b)}
        .filter { case (a, b) => clustersCriterion(a, b)}
        .toSet
    couplesToMerge.foldLeft(initialClusters)(mergeSubSets)
  }

  /**
   * Merge the two subsets that contains
   * the two parts of the tuple, if they are not already
   * in the same subset
   */
  private def mergeSubSets[A](setOfSets: Set[Set[A]], coupleToMerge: (A, A)): Set[Set[A]] = {
    val (a, b) = coupleToMerge
    val subsetA = findSubset(setOfSets, a)
    val subsetB = findSubset(setOfSets, b)
    if(subsetA == subsetB)
      setOfSets
    else
      setOfSets + (subsetA ++ subsetB) - subsetA - subsetB
  }

  /**
   * Find the subset that contains a
   */
  private def findSubset[A](setOfSets: Set[Set[A]], a: A): Set[A] =
    setOfSets find (_.contains(a)) getOrElse (
      throw new RuntimeException(f"the value $a was not contained in any subset of $setOfSets")
    )






}
