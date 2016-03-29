package graph;

import graph.ProbabilityDistributionAlgorithm.CreditCalculator;
import graph.ProbabilityDistributionAlgorithm.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import utilities.StreamUtilities;

/**
 * A class for estimating the node probability distribution after a specified
 * number of steps, by performing multiple random walks and tallying the number
 * of hits each node gets at each time interval.
 */
abstract class DistributionEstimator {
  private final ReadableNode<?> startNode;
  final int categories;
  final Map<ReadableNode<?>, int[]> counts = new HashMap<>();
  private final CreditCalculator creditCalculator;
  private final Method method;
  private final int k;

  DistributionEstimator(
    ReadableGraph<?> readableGraph,
    int categories,
    CreditCalculator creditCalculator,
    Method method,
    int k)
  {
    this.startNode =
      readableGraph.getNodes()
        .collect(StreamUtilities.<ReadableNode<?>>randomElementCollector());
    this.categories = categories;
    this.creditCalculator = creditCalculator;
    this.method = method;
    this.k = k;
  }

  /**
   * Perform a random walk and add the visited nodes to the running counts.
   */
  void performWalk(int steps) {
    ReadableNode<?> currentNode = startNode;
    recordNodeVisit(currentNode, 0);
    for (int step = 1; step <= steps; step++) {
      Map<ReadableNode<?>, Double> probabilities =
        ProbabilityDistributionAlgorithm
          .getNeighborVector(currentNode, k, creditCalculator, method);
      double random = ThreadLocalRandom.current().nextDouble();
      for (Entry<ReadableNode<?>, Double> entry : probabilities.entrySet()) {
        random -= entry.getValue();
        if (random < 0) {
          currentNode = entry.getKey();
          break;
        }
      }
      recordNodeVisit(currentNode, step);
    }
  }

  /**
   * Return an {@code int[]}, with position i indicating the number of times we
   * visited the specified node on step i of the walk.
   */
  int[] getNodeProbabilities(ReadableNode<?> readableNode) {
    int[] result = counts.get(readableNode);
    return
      result == null
        ? new int[categories]
        : Arrays.copyOf(result, result.length);
  }

  abstract void recordNodeVisit(ReadableNode<?> node, int step);
}
