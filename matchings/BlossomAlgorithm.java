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
		Graph graph = matching.myGraph;
		ArrayList<Node> nodeList = graph.getNodeList();
		ArrayList<Edge> edgeList = graph.getEdgeList();
		Forest forest = new Forest();
		
		for (Node node : nodeList) {
			node.unmark();
		}
		
		for (Edge edge : edgeList) {
			if (matching.hasEdge(edge)) edge.mark();
			else edge.unmark();
		}
		
		for (Node node : nodeList) {
			if(matching.isExposed(node)) {
				forest.addNewTree(node);
			}
		}
		
		while (forest.getUnmarkedVertexWithEvenDistanceToRoot() != null) {
			Node v = forest.getUnmarkedVertexWithEvenDistanceToRoot();
			while (forest.getUnmarkedEdgeIncidentToNode(v) != null) {
				Edge e = forest.getUnmarkedEdgeIncidentToNode(v);
				Node w = e.getOther(v);
				
				if (!forest.containsNode(w)) {
					Node x = matching.getIncidentNode(w);
					forest.addNewEdge(v, w);
					forest.addNewEdge(w, x);
				}
				
				else {
					if (forest.getDistanceToRoot(w) % 2 == 1) {
						// do nothing
					}
					else {
						if (forest.getRootOfNode(v) != forest.getRootOfNode(w)) {
							ArrayList<Edge> path = new ArrayList<Edge>();
							ArrayList<Edge> pathFromRootToV = forest.getPathFromRootToNode(v);
							ArrayList<Edge> pathFromWToRoot = forest.getPathFromNodeToRoot(w);
							for (Edge edge : pathFromRootToV) path.add(edge);
							path.add(e); //e = v -> w
							for (Edge edge : pathFromWToRoot) path.add(edge);
							return path;
						}
						else {
							////v TODO v////
						
							////^ TODO ^////
						}
					}
				}
				e.mark();
			}
			v.mark();
		}	
		return new ArrayList<Edge>();
	}

}
