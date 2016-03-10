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
final class DistributionEstimator {
  private final ReadableNode<?> startNode;
  private final Map<ReadableNode<?>, int[]> counts = new HashMap<>();
  private int walks;
  private final int steps;
  private final CreditCalculator creditCalculator;
  private final Method method;
  private final int k;

  DistributionEstimator(
    ReadableGraph<?> readableGraph,
    int steps,
    CreditCalculator creditCalculator,
    Method method,
    int k)
  {
    this.startNode =
      readableGraph.getNodes()
        .collect(StreamUtilities.<ReadableNode<?>>randomElementCollector());
    this.steps = steps;
    this.creditCalculator = creditCalculator;
    this.method = method;
    this.k = k;
  }

  /**
   * Perform a random walk and add the visited nodes to the running counts.
   */
  void performWalk() {
    ReadableNode<?> currentNode = startNode;
    int[] stepsArray = counts.get(startNode);
    if (stepsArray == null) {
      stepsArray = new int[steps + 1];
      counts.put(startNode, stepsArray);
    }
    stepsArray[0]++;
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
      stepsArray = counts.get(currentNode);
      if (stepsArray == null) {
        stepsArray = new int[steps + 1];
        counts.put(currentNode, stepsArray);
      }
      stepsArray[step]++;
    }
    walks++;
  }

  /**
   * Return an {@code int[]}, with position i indicating the number of times we
   * visited the specified node on step i of the walk.
   */
  int[] getNodeProbabilities(ReadableNode<?> readableNode) {
    int[] result = counts.get(readableNode);
    return
      result == null
        ? new int[steps + 1]
        : Arrays.copyOf(result, result.length);
  }
}
