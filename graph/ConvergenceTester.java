package graph;

import java.util.function.Predicate;

/**
 * A class for testing how quickly a Markov chain converges to a stationary
 * distribution.
 */
final class ConvergenceTester<T> {
	private TransitionMatrix<T> transitionMatrix;
	private int iterations = 0;
	
	private ConvergenceTester(TransitionMatrix<T> transitionMatrix) {
		this.transitionMatrix = transitionMatrix;
	}
	
	static <T> ConvergenceTester<T> forTransitionMatrix(
		TransitionMatrix<T> transitionMatrix)
	{
		return new ConvergenceTester<>(transitionMatrix);
	}
	
	TransitionMatrix<T> getTransitionMatrix() {
		return transitionMatrix;
	}
	
	/**
	 * Repeatedly square the matrix, and print the diameter of convergence for
	 * each iteration.
	 */
	void printInfiniteIteration(Predicate<ReadableNode<T>> nodesToInclude) {
		while (true) {
			System.out.print("2^");
			System.out.print(iterations);
			System.out.print(" steps; distance ");
			System.out.println(convergenceUniformDistance(nodesToInclude));
			iterateDistributions(1);
		}
	}
	
	/**
	 * Repeatedly square the matrix.
	 * 
	 * @param iterations The number of times to square the matrix.
	 */
	void iterateDistributions(int iterations) {
		while (iterations-- > 0) {
			transitionMatrix = transitionMatrix.square();
			this.iterations++;
		}
	}
	
	/**
	 * Determine the width of the band where the distribution of the Markov
	 * chain can be located after 2^{@link iterations} steps.
	 * 
	 * @return The max range of any coordinate of the distribution vector.
	 */
	double convergenceUniformDistance(Predicate<ReadableNode<T>> nodesToInclude) {
		return transitionMatrix.distributionRange(nodesToInclude);
	}

  double convergenceTaxicabDistance(Predicate<ReadableNode<T>> nodesToInclude) {
    return transitionMatrix.distributionTaxicab(nodesToInclude);
  }

  /**
   * Compute, given the worst possible initial probability distribution, the
   * lowest probability of being on any given node.
   */
  double minNodeProbability() {
    return transitionMatrix.minEntry();
  }
	
	/**
	 * Compute the ceiling of the log of the number of steps needed to force the
	 * distribution to converge within the specified margin of error.
	 * 
	 * @param convergenceDistance The maximum permitted range in the probability
	 * of any state when starting from different distributions and running the
	 * Markov chain the computed number of times.
	 * @return The log_2 of the number of steps we need to run the Markov chain
	 * to get within the specified error.
	 */
	int logStepsForConvergence(
    Predicate<ReadableNode<T>> nodesToInclude,
    double convergenceDistance)
  {
		int result = 0;
		while (
      transitionMatrix.distributionRange(nodesToInclude) > convergenceDistance)
    {
			transitionMatrix = transitionMatrix.square();
			result++;
		}
		return result;
	}
}
