import java.util.HashSet;
import java.util.Set;

public class NodeCoverRunner {
	
	private final static int k = 3;
	
	public static int run(Graph g, Node s) {
		
		Set<Node> coveredNodes = new HashSet<Node>();
		coveredNodes.add(s);
		Node currentNode = s;
		
		for (int i = 0; true; i++) {
			if (coveredNodes.size() == g.getNodeCnt()) return i;
			
			double[] probDist = ProbabilityDistributionAlgorithm.run(currentNode, k);
			currentNode = transition(currentNode, probDist);
		}

	}
	
	private static Node transition(Node v, double[] probDist) {
		double rand = Math.random();
		double threshold = 0;
		for (int i = 0; i < v.getAdj().size(); i++) {
			Node u = v.getAdj().get(i);
			double prob = probDist[i];
			
			threshold += prob;
			if (rand < threshold) return u;
		}
		return null;
	}

}
