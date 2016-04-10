package matchings;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class BlossomAlgorithm {

	/** Runs the Blossom Algorithm specified here:
	 * https://en.wikipedia.org/wiki/Blossom_algorithm
	 * 
	 * It modifies the input matching in place. When it terminates,
	 * the matching will be maximal.
	 */
	public static void runAlgorithm(Matching matching) {
		while (true) {
			ArrayList<Edge> augPath = findAugPath(matching);
			if (augPath.isEmpty()) break;
			else augment(matching, augPath);
		}
	}

	/** Augments the augPath onto the matching, by essentially
	 *  toggling whether each edge in the path is contained
	 *  in the matching or not.
	 */
	protected static void augment(Matching matching, ArrayList<Edge> augPath) {
		//remove all odd edges of the path
		for(int i = 1; i < augPath.size(); i += 2) {
			Edge edge = augPath.get(i);
			matching.removeEdge(edge);
		}
		//add all even edges of the path
		for(int i = 0; i < augPath.size(); i += 2) {
			Edge edge = augPath.get(i);
			matching.addEdge(edge);
		}
	}

	/** Returns an augmenting path for the matching, or an empty list
	 *  if no possible path can be found.
	 */
	protected static ArrayList<Edge> findAugPath(Matching matching) {
		ArrayList<Edge> output = new ArrayList<Edge>();
		Graph graph = matching.myGraph;
		ArrayList<Node> nodeList = graph.getNodeList();
		ArrayList<Edge> edgeList = graph.getEdgeList();
		Forest forest = new Forest();
		
		Map<Node,Boolean> markedNodes = new TreeMap<Node,Boolean>();
		Map<Edge,Boolean> markedEdges = new TreeMap<Edge,Boolean>();

		for (Node node : nodeList) {
			markedNodes.put(node, false);
		}
		
		for (Edge edge : edgeList) {
			markedEdges.put(edge, matching.hasEdge(edge));
		}
		
		for (Node node : nodeList) {
			if(matching.isExposed(node)) {
				forest.addNewTree(node);
			}
		}
				
		return output;
	}

}
