import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * A class representing a transition matrix in a Markov chain.
 */
class TransitionMatrix {
  private static final IntFunction<double[][]> ARRAY_GENERATOR =
    double[][]::new;

  /**
   * Each {@code double[]} is a vector representing the probabilities of leaving
   * a certain vertex to each of the other vertices.
   */
  private final double[][] transitionVectors;

  private TransitionMatrix(double[][] transitionVectors) {
    this.transitionVectors = transitionVectors;
  }

  /**
   * Construct a {@link TransitionMatrix} using a copy of the given data.
   */
  static TransitionMatrix fromTransitionVectors(double[][] transitionVectors) {
    return new TransitionMatrix(copy(transitionVectors));
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
   * Return the matrix product of this matrix with itself.
   */
  TransitionMatrix square() {
    return
      new TransitionMatrix(
        Arrays.stream(transitionVectors)
          .map(
            vector ->
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
                .toArray())
          .toArray(double[][]::new));
  }
  
  /**
   * Compute the maximum over all nodes of the range of possible distribution
   * probabilities for each node after one step in the Markov chain.
   * 
   * @return The max probability range for a node after one step as the
   * initial distribution varies.
   */
  double distributionRange() {
    return
      IntStream.range(0, transitionVectors.length)
        .mapToDouble(this::nodeRange)
        .max()
        .getAsDouble();
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
}
