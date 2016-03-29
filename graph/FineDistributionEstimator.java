package graph;

import utilities.StreamUtilities;

public class FineDistributionEstimator extends DistributionEstimator {
  FineDistributionEstimator(
    ReadableGraph<?> readableGraph,
    int steps,
    ProbabilityDistributionAlgorithm.CreditCalculator creditCalculator,
    ProbabilityDistributionAlgorithm.Method method,
    int k)
  {
    super(readableGraph, steps + 1, creditCalculator, method, k);
  }

  @Override
  void recordNodeVisit(ReadableNode<?> node, int step) {
    int[] stepsArray = counts.get(node);
    if (stepsArray == null) {
      stepsArray = new int[categories];
      counts.put(node, stepsArray);
    }
    stepsArray[step]++;
  }

  void performWalks(int walkCount) {
    while (walkCount-- > 0) {
      performWalk(categories - 1);
    }
  }
}
