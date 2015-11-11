/**
 * A class for testing how quickly a Markov chain converges to a stationary
 * distribution.
 */
final class ConvergenceTester {
	private TransitionMatrix transitionMatrix;
	private int iterations = 0;
	
	private ConvergenceTester(TransitionMatrix transitionMatrix) {
		this.transitionMatrix = transitionMatrix;
	}
	
	static ConvergenceTester forTransitionMatrix(
		TransitionMatrix transitionMatrix)
	{
		return new ConvergenceTester(transitionMatrix);
	}
	
	static ConvergenceTester forTransitionMatrix(
		double[][] transitionVectors)
	{
		return
			new ConvergenceTester(
				TransitionMatrix.fromTransitionVectors(transitionVectors));
	}
	
	TransitionMatrix getTransitionMatrix() {
		return transitionMatrix;
	}
	
	/**
	 * Repeatedly square the matrix, and print the diameter of convergence for
	 * each iteration.
	 */
	void printInfiniteIteration() {
		while (true) {
			System.out.print("2^");
			System.out.print(iterations);
			System.out.print(" steps; distance ");
			System.out.println(convergenceDistance());
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
	double convergenceDistance() {
		return transitionMatrix.distributionRange();
	}
}
