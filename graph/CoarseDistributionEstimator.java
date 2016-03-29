package graph;

final class CoarseDistributionEstimator extends DistributionEstimator {
  CoarseDistributionEstimator(
    ReadableGraph<?> readableGraph,
    int logSteps,
    ProbabilityDistributionAlgorithm.CreditCalculator creditCalculator,
    ProbabilityDistributionAlgorithm.Method method,
    int k)
  {
    super(readableGraph, logSteps + 1, creditCalculator, method, k);
  }

  @Override
  void recordNodeVisit(ReadableNode<?> node, int step) {
    int[] stepsArray = counts.get(node);
    if (stepsArray == null) {
      stepsArray = new int[categories];
      counts.put(node, stepsArray);
    }
    stepsArray[32 - Integer.numberOfLeadingZeros(step)]++;
  }

  void performWalks(int fullWalkCount) {
    int walkCount = fullWalkCount;
    boolean firstTime = true;
    for (
      int walkLength = (1 << categories - 1) - 1;
      walkLength > 0;
      walkLength >>= 1)
    {
      for (int walksLeft = walkCount; walksLeft > 0; walksLeft--) {
        performWalk(walkLength);
      }
      if (firstTime) {
        firstTime = false;
      }
      else {
        walkCount <<= 1;
      }
    }
  }
}
