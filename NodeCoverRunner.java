import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NodeCoverRunner {
	
	private final static int k = 3;
	
	public static int run(Graph g, Node s) {
		
		Set<Node> coveredNodes = new HashSet<Node>();
		coveredNodes.add(s);
		Node currentNode = s;
		
		for (int i = 0; true; i++) {
			if (coveredNodes.size() == g.getNodeCnt()) return i;
			
			Map<Node, Double> probDist =
				ProbabilityDistributionAlgorithm
					.getNeighborVector(currentNode, k);
			currentNode = transition(probDist);
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
