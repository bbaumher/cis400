package graph;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A class representing a transition matrix in a Markov chain.
 */
class TransitionMatrix<T> {
  /**
   * Each {@code double[]} is a vector representing the probabilities of leaving
   * a certain vertex to each of the other vertices.
   */
  private final double[][] transitionVectors;
  private final List<ReadableNode<T>> orderedNodes;

  private TransitionMatrix(
    double[][] transitionVectors,
    List<ReadableNode<T>> orderedNodes)
  {
    this.transitionVectors = transitionVectors;
    this.orderedNodes = orderedNodes;
  }
  
  /**
   * Determine the {@link TransitionMatrix} for a graph by using a nested
   * function to query node transition probabilities. In effect, this discards
   * the knowledge about which probability corresponds to which nodes. However,
   * for debugging purposes, the order of the rows and columns will be the
   * numerical order of the node ids.
   * 
   * @param nodes A {@link Stream} returning the {@link Node}s for which to
   * build the {@link TransitionMatrix} (presumably all {@link Node}s in the
   * graph).
   * @param probabilityRetriever A {@link Function} that takes in the source
   * {@link Node} and returns a {@link ToDoubleFunction} for computing the
   * probability of transitioning to each target {@link Node}.
   * @return The {@link TransitionMatrix} constructed for the provided
   * parameters.
   */
  static <T> TransitionMatrix<T> fromProbabilityRetriever(
    Stream<? extends ReadableNode<T>> nodes,
    Function<ReadableNode<T>, ToDoubleFunction<ReadableNode<T>>>
      probabilityRetriever)
  {
    List<ReadableNode<T>> orderedNodes =
      // Sorting by ID is unnecessary but makes debugging easier.
      nodes.sorted(
          (node1, node2) ->
            Integer.compare(node1.getId().hashCode(), node2.getId().hashCode()))
        .collect(Collectors.<ReadableNode<T>>toList());
    return
      new TransitionMatrix<>(
        orderedNodes.stream()
          .map(probabilityRetriever)
          .map(
            function -> orderedNodes.stream().mapToDouble(function).toArray())
          .toArray(double[][]::new),
        orderedNodes);
  }
  
  /**
   * Construct a {@link TransitionMatrix} corresponding to the transition
   * probabilities given in the maps. In effect, this discards the knowledge
   * about which probability corresponds to which nodes. However, for debugging
   * purposes, the order of the rows and columns will be the numerical order of
   * the node ids.
   * 
   * @param transitionMaps The info about transition probabilities.
   * {@code transitionMaps.get(i).get(j)} should return the probability of going
   * to {@link Node} {@code j} after being at {@link Node} {@code i}.
   */
  static <T> TransitionMatrix<T> fromTransitionMaps(
    Map<ReadableNode<T>, Map<ReadableNode<T>, Double>> transitionMaps)
  {
    return
		fromProbabilityRetriever(
			transitionMaps.keySet().stream(),
			source -> {
				Map<ReadableNode<T>, Double> map = transitionMaps.get(source);
				return target -> map.getOrDefault(target, 0d);
			}
		);
  }
  
  /**
   * Return a deep copy of the given array.
   */
  private static double[][] copy(double[][] array) {
    return
      Arrays.stream(array)
        .map(a -> Arrays.copyOf(a, a.length))
        .toArray(double[][]::new);
  }
  
  /**
   * Get a copy of the transition vector array.
   */
  double[][] getTransitionVectors() {
    return copy(transitionVectors);
  }

  /**
   * Return the matrix product of this matrix with itself. Also, normalize each
   * row to prevent small rounding errors to propagate too far.
   */
  TransitionMatrix<T> square() {
    return
      new TransitionMatrix<>(
        Arrays.stream(transitionVectors)
          .parallel()
          .map(
            vector -> {
              double[] result =
                IntStream.range(0, vector.length)
                  .mapToDouble(
                    destinationIndex ->
                      IntStream.range(0, vector.length)
                        .mapToDouble(
                          intermediateIndex ->
                            vector[intermediateIndex]
                              * transitionVectors[intermediateIndex]
                                  [destinationIndex])
                        .sum())
                  .toArray();
              ProbabilityDistributionAlgorithm.normalize(result);
              return result;
            }
          )
          .toArray(double[][]::new),
        orderedNodes);
  }
  
  /**
   * Compute the maximum over all nodes of the range of possible distribution
   * probabilities for each node after one step in the Markov chain.
   * 
   * @return The max probability range for a node after one step as the
   * initial distribution varies.
   */
  double distributionRange(Predicate<ReadableNode<T>> nodesToInclude) {
    return
      IntStream.range(0, transitionVectors.length)
        .filter(nodeIndex -> nodesToInclude.test(orderedNodes.get(nodeIndex)))
        .mapToDouble(this::nodeRange)
        .max()
        .getAsDouble();
  }

  double distributionTaxicab(Predicate<ReadableNode<T>> nodesToInclude) {
    return
      IntStream.range(0, transitionVectors.length)
        .filter(nodeIndex -> nodesToInclude.test(orderedNodes.get(nodeIndex)))
        .mapToDouble(this::nodeRange)
        .sum();
  }
  
  /**
   * Compute the range of distribution probabilities for the given node among
   * all possible distributions after one step.
   * 
   * @param nodeIndex The index of the node whose occurrence we are analyzing.
   * @return The difference between the largest and smallest possible
   * probabilities for the vertex.
   */
  private double nodeRange(int nodeIndex) {
    Supplier<DoubleStream> streamSupplier =
      () ->
        Arrays.stream(transitionVectors)
          .mapToDouble(vector -> vector[nodeIndex]);
    double min = streamSupplier.get().min().getAsDouble();
    double max = streamSupplier.get().max().getAsDouble();
    return max - min;
  }

  /**
   * Return the value in the minimum cell in the matrix.
   */
  double minEntry() {
    return
      Arrays.stream(transitionVectors)
        .mapToDouble(
          vector ->
            Arrays.stream(vector).min().orElse(Double.POSITIVE_INFINITY))
        .min()
        .orElse(Double.POSITIVE_INFINITY);
  }

  void processEntries(
    int row,
    BiConsumer<ReadableNode<T>, Double> entryConsumer)
  {
    double[] rowEntries = transitionVectors[row];
    for (int column = 0; column < orderedNodes.size(); column++) {
      entryConsumer.accept(orderedNodes.get(column), rowEntries[column]);
    }
  }
}
