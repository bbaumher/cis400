
import java.util.Set;

/**
 * Contains a static class for constructing the {@link TransitionMatrix} for a
 * standard random walk on a graph.
 */
final class UniformDistributionAlgorithm {
	private UniformDistributionAlgorithm() { }
	
	static TransitionMatrix getTransitionMatrix(ReadableGraph graph) {
		return
			TransitionMatrix.fromProbabilityRetriever(
				graph.getNodes(),
				source -> {
					Set<? extends ReadableNode> neighbors = source.getAdjSet();
					double result = 1d / neighbors.size();
					return target -> neighbors.contains(target) ? result : 0d;
				}
			);
	}
}
