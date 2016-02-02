package matchings;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphAssessor {
	
	public static void assess(Graph graph) {
		
		Map<Integer,Integer> degreeCounts = new TreeMap<Integer,Integer>();
		Map<Integer,Integer> longestPathLengthCounts = new TreeMap<Integer,Integer>();
		
		for (Node node : graph.getNodeList()) {
			int degree = node.getEdgeList().size();
			int longestPathLength = getLongestPathLength(node);
			increment(degreeCounts, degree);
			increment(longestPathLengthCounts, longestPathLength);
		}
		
		System.out.println("# nodes: " + graph.getNodeCount());
		
		System.out.println("# edges: " + graph.getEdgeCount());
		
		double averageDegree = 2.0 * graph.getEdgeCount() / graph.getNodeCount();
		System.out.println("avg degree: " + averageDegree);
		
		int totalPossibleEdges = graph.getNodeCount() * (graph.getNodeCount()-1) / 2;
		double density = graph.getEdgeCount() / (double) totalPossibleEdges;
		System.out.println("density: " + density);
		
		System.out.println("degree counts:");
		for (int key : degreeCounts.keySet()) {
			System.out.println(key + "\t" + degreeCounts.get(key));
		}
				
		System.out.println("longest path length counts:");
		for (int key : longestPathLengthCounts.keySet()) {
			System.out.println(key + "\t" + longestPathLengthCounts.get(key));
		}
		
	}
	
	private static int getLongestPathLength(Node seed) {
		Queue<Node> queue = new LinkedList<Node>();
		Queue<Node> next = new LinkedList<Node>();
		Set<Node> seen = new TreeSet<Node>();
		
		queue.add(seed);
		seen.add(seed);
		
		int dist;
		for (dist = 0; !queue.isEmpty(); dist++) {
			while (!queue.isEmpty()) {
				Node dequeue = queue.poll();
				for (Node neighbor : dequeue.getNeighbors()) {
					if (seen.contains(neighbor)) continue;
					next.add(neighbor);
					seen.add(neighbor);
				}
			}
			
			queue.addAll(next);
			next.clear();
		}
		return dist;
	}

	private static void increment(Map<Integer,Integer> map, int key) {
		if (map.containsKey(key)) {
			int value = map.get(key);
			map.put(key, value+1);
		}
		else {
			map.put(key, 1);
		}
	}

}
