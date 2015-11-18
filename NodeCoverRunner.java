import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NodeCoverRunner {
	
	private static final double COVER_THRESHOLD = 0.99; //proportion of nodes to cover
		
	public static int getCoverTime(Graph g, Node s, int k) {
		
		Set<Node> coveredNodes = new HashSet<Node>();
		coveredNodes.add(s);
		Node currentNode = s;
		
		long startTime = System.currentTimeMillis();
		for (int i = 0; true; i++) {
			if (coveredNodes.size() >= COVER_THRESHOLD * g.getNodeCnt()) {
				return i;
			}
			
			Map<Node, Double> probDist =
				ProbabilityDistributionAlgorithm
					.getNeighborVector(currentNode, k);
			currentNode = transition(probDist);
			
			if (!coveredNodes.contains(currentNode)) {
				coveredNodes.add(currentNode);
				//System.out.println(coveredNodes.size());
			}
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > 30 * 1000) return i;
		}

	}
	
	private static Node transition(Map<Node, Double> probDist) {
		double rand = Math.random();
		double threshold = 0;
		for (Map.Entry<Node, Double> entry : probDist.entrySet()) {
			Node u = entry.getKey();
			double prob = entry.getValue();
			
			threshold += prob;
			if (rand < threshold) return u;
		}
		return null;
	}

}
