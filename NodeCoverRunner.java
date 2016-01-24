import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class NodeCoverRunner {
	
	private static final double COVER_THRESHOLD = 1; //proportion of nodes to cover
	
	private final Random random;
  private final ProbabilityDistributionAlgorithm.CreditCalculator
    creditCalculator;
	
	NodeCoverRunner(
    Random random,
    ProbabilityDistributionAlgorithm.CreditCalculator creditCalculator)
  {
		this.random = random;
    this.creditCalculator = creditCalculator;
	}
		
	public <T> int getCoverTime(ReadableGraph<T> g, ReadableNode<T> s, int k) {
		Map<ReadableNode<?>, Map<ReadableNode<?>, Double>> transitions =
      new HashMap<>();
    g.getNodes()
      .forEach(
        node ->
          transitions.put(
            node,
            ProbabilityDistributionAlgorithm.getNeighborVector(
              node,
              k,
              creditCalculator)));
      
    Iterator<ReadableGraph<T>> iterator =
      SCCTester.getStronglyConnectedComponents(
        ReadableGraph.getSubGraph(
          g,
          edge -> transitions.get(edge.getStart()).get(edge.getEnd()) > 0));

    ReadableGraph<T> graph = iterator.next();
    while (graph.getNode(s.getId()) == null) {
      graph = iterator.next();
    }
    if (graph.getNodeCnt() < g.getNodeCnt()) {
      System.out
        .println(g.getNodeCnt() - graph.getNodeCnt() + " node(s) unreachable:");
    }

		Set<ReadableNode<?>> coveredNodes = new HashSet<>();
		coveredNodes.add(s);
		ReadableNode<?> currentNode = s;
		
		long startTime = System.currentTimeMillis();
		for (int i = 0; true; i++) {
			if (coveredNodes.size() >= COVER_THRESHOLD * graph.getNodeCnt()) {
				return i;
			}
			
			Map<ReadableNode<?>, Double> probDist = transitions.get(currentNode);
			currentNode = transition(probDist);
			
			if (!coveredNodes.contains(currentNode)) {
				coveredNodes.add(currentNode);
				//System.out.println(coveredNodes.size());
			}
			
//			long currentTime = System.currentTimeMillis();
//			if (currentTime - startTime > 30 * 1000) return i;
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
