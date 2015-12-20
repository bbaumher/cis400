import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class NodeCoverRunner {
	
	private static final double COVER_THRESHOLD = 0.99; //proportion of nodes to cover
	
	private final Random random;
	
	NodeCoverRunner(Random random) {
		this.random = random;
	}
		
	public int getCoverTime(Graph<?> g, Node<?> s, int k) {
		
		Set<ReadableNode<?>> coveredNodes = new HashSet<>();
		coveredNodes.add(s);
		ReadableNode<?> currentNode = s;
		
		long startTime = System.currentTimeMillis();
		for (int i = 0; true; i++) {
			if (coveredNodes.size() >= COVER_THRESHOLD * g.getNodeCnt()) {
				return i;
			}
			
			Map<ReadableNode<?>, Double> probDist =
				ProbabilityDistributionAlgorithm
					.getNeighborVector(
						currentNode,
						k,
						ProbabilityDistributionAlgorithm::calculateCredits);
			currentNode = transition(probDist);
			
			if (!coveredNodes.contains(currentNode)) {
				coveredNodes.add(currentNode);
				//System.out.println(coveredNodes.size());
			}
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > 30 * 1000) return i;
		}

	}
	
	private ReadableNode<?> transition(Map<ReadableNode<?>, Double> probDist) {
		double rand = random.nextDouble();
		double threshold = 0;
		for (Map.Entry<ReadableNode<?>, Double> entry : probDist.entrySet()) {
			ReadableNode<?> u = entry.getKey();
			double prob = entry.getValue();
			
			threshold += prob;
			if (rand < threshold) return u;
		}
		return null;
	}

}
