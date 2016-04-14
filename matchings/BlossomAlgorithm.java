package matchings;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
			while (graph.getUnmarkedEdgeIncidentToNode(v) != null) {
				Edge e = graph.getUnmarkedEdgeIncidentToNode(v);
				Node w = e.getOther(v);
				//e = (v,w)
				
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
						else {//the blossom case
							
							//create blossom (with edges and nodes)
							ArrayList<Edge> blossomEdges = new ArrayList<Edge>();
							blossomEdges.add(e);
							for (Edge edge : forest.getPath(v, w)) blossomEdges.add(edge);
							Set<Node> blossomNodes = new TreeSet<Node>();
							for (Edge edge : blossomEdges) {
								blossomNodes.add(edge.getNode1());
								blossomNodes.add(edge.getNode2());
							}
							ArrayList<Node> blossomNodePath = getNodePath(blossomEdges);
							blossomNodePath.remove(blossomNodePath.size()-1); //remove end so no repeats
							
							//create contracted graph2
							Graph graph2 = new Graph(graph.getNodeCount() - blossomNodes.size() + 1, "z");
							
							//map nodes in graph to graph2
							Node blossomNode = graph2.getNodeIndexedAt(0);
							Map<Node,Node> nodeToNode2 = new TreeMap<Node,Node>();
							for (Node node : blossomNodes) {
								nodeToNode2.put(node, blossomNode);
							}
							int index = 1;
							for (Node node : graph.getNodeList()) {
								if (blossomNodes.contains(node)) continue;
								nodeToNode2.put(node, graph2.getNodeIndexedAt(index));
								index++;
							}
							
							//map nodes in graph2 to graph (blossom nodes points to null)
							Map<Node,Node> node2ToNode = new TreeMap<Node,Node>();
							for (Node node : graph.getNodeList()) {
								Node node2 = nodeToNode2.get(node);
								if (blossomNodes.contains(node)) {
									node2ToNode.put(node2, null);
								}
								else {
									node2ToNode.put(node2, node);
								}
							}
							
							//add edges to graph2 to make it just like graph
							for (Node node : graph.getNodeList()) {
								for (Node neighbor : node.getNeighbors()) {
									Node node2 = nodeToNode2.get(node);
									Node neighbor2 = nodeToNode2.get(neighbor);
									if (node2 == neighbor2) continue;
									graph2.addEdgeBetweenNodes(node2, neighbor2);
								}
							}
							
							//create matching2 for graph2 to mimic matching for graph
							Matching matching2 = new Matching(graph2);
							for (Node node : graph.getNodeList()) {
								for (Node neighbor : node.getNeighbors()) {
									Edge edge = node.getEdgeTo(neighbor);
									Node node2 = nodeToNode2.get(node);
									Node neighbor2 = nodeToNode2.get(neighbor);
									if (node2 == neighbor2) continue;
									Edge edge2 = node2.getEdgeTo(neighbor2);
									if (matching.hasEdge(edge)) matching2.addEdge(edge2);
								}
							}
							
							//get ready to lift path2 to make path
							ArrayList<Edge> edgePath2 = findAugPath(matching2);
							ArrayList<Node> nodePath2 = getNodePath(edgePath2);
							
							//determine if path2 goes through the blossom node
							boolean pathGoesThroughBlossomNode = false;
							int blossomNodeIndex = -1;
							for (int i = 0; i < nodePath2.size(); i++) {
								Node node2 = nodePath2.get(i);
								if (node2 == blossomNode) {
									pathGoesThroughBlossomNode = true;
									blossomNodeIndex = i;
									break;
								}
							}
							
							if (!pathGoesThroughBlossomNode) {
								ArrayList<Node> nodePath = new ArrayList<Node>();
								for (Node node2 : nodePath2) {
									Node node = node2ToNode.get(node2);
									nodePath.add(node);
								}
								ArrayList<Edge> edgePath = getEdgePath(nodePath);
								return edgePath;
							}
							else { //if (pathGoesThroughBlossomNode) {
								
								//create path leading up to blossom
								ArrayList<Node> prePath = new ArrayList<Node>();
								for (int i = 0; i < blossomNodeIndex; i++) {
									Node node2 = nodePath2.get(i);
									Node node = node2ToNode.get(node2);
									prePath.add(node);
								}
								
								//create path leading away from blossom
								ArrayList<Node> postPath = new ArrayList<Node>();
								for (int i = blossomNodeIndex+1; i < nodePath2.size(); i++) {
									Node node2 = nodePath2.get(i);
									Node node = node2ToNode.get(node2);
									postPath.add(node);
								}
								
								//iterate through all possible paths in the blossom
								for (int startIndex = 0; startIndex < blossomNodePath.size(); startIndex++) {
									for (int endIndex = startIndex+1; endIndex < blossomNodePath.size(); endIndex++) {
										for (int k = 0; k < 2; k++) {
											boolean rightwards = (k == 0) ? true : false;
											ArrayList<Node> midPath = getNodeSubpath(blossomNodePath, startIndex, endIndex, rightwards);
											
											//concatenate the path before, in, and after the blossom
											ArrayList<Node> concatNodePath = concatenateNodePaths(prePath, midPath, postPath);
											ArrayList<Edge> concatEdgePath = getEdgePath(concatNodePath);
											
											//determine if this is a valid alternating path
											if (concatEdgePath == null) continue; //transition in/out of blossom has no edge
											if (!isValidAlternatingPath(concatEdgePath, matching)) continue; //not alternating
											return concatEdgePath;
										}
									}
								}
								return null; //this should never happen!
							}
						}
					}
				}
				e.mark();
			}
			v.mark();
		}
		return new ArrayList<Edge>();
	}
	
	/** Takes a path of edges and converts it to a path of nodes.
	 */
	protected static ArrayList<Node> getNodePath(ArrayList<Edge> edgePath) {
		ArrayList<Node> nodePath = new ArrayList<Node>();
		if (edgePath.size() == 0) {
			return nodePath;
		}
		if (edgePath.size() == 1) {
			Edge edge = edgePath.get(0);
			nodePath.add(edge.getNode1());
			nodePath.add(edge.getNode2());
			return nodePath;
		}
		
		Edge edge0 = edgePath.get(0);
		Edge edge1 = edgePath.get(1);
		Node linkingNode = null;
		if (edge0.getNode1() == edge1.getNode1()) linkingNode = edge0.getNode1();
		else if (edge0.getNode1() == edge1.getNode2()) linkingNode = edge0.getNode1();
		else if (edge0.getNode2() == edge1.getNode1()) linkingNode = edge0.getNode2();
		else if (edge0.getNode2() == edge1.getNode2()) linkingNode = edge0.getNode2();
		
		Node firstNode = edge0.getOther(linkingNode);
		nodePath.add(firstNode);
		nodePath.add(linkingNode);
		
		for (int i = 1; i < edgePath.size(); i++) {
			Edge edge = edgePath.get(i);
			linkingNode = edge.getOther(linkingNode);
			nodePath.add(linkingNode);
		}
		
		return nodePath;
	}
	
	/** Takes a path of nodes and converts it to a path of edges.
	 *  Returns null if two consecutive nodes don't have an edge between them.
	 */
	protected static ArrayList<Edge> getEdgePath(ArrayList<Node> nodePath) {
		ArrayList<Edge> edgePath = new ArrayList<Edge>();
		for (int i = 0; i < nodePath.size()-1; i++) {
			Node node1 = nodePath.get(i);
			Node node2 = nodePath.get(i+1);
			Edge edge = node1.getEdgeTo(node2);
			if (edge == null) return null;
			edgePath.add(edge);
		}
		return edgePath;
	}
	
	/** Get a subpath of a node path. It's inclusive. It allows for wrapping.
	 */
	protected static ArrayList<Node> getNodeSubpath(ArrayList<Node> nodePath, int startIndex, int endIndex, boolean rightwards) {
		ArrayList<Node> nodeSubpath = new ArrayList<Node>();
		int n = nodePath.size();
		for (int i = startIndex; true; i = ((i + (rightwards ? 1 : -1) + n) % n)) {
			nodeSubpath.add(nodePath.get(i));
			if (i == endIndex) break;
		}
		return nodeSubpath;
	}
	
	/** Concatenate 3 node paths.
	 */
	protected static ArrayList<Node> concatenateNodePaths(ArrayList<Node> nodePath1, ArrayList<Node> nodePath2, ArrayList<Node> nodePath3) {
		ArrayList<Node> nodePathConcat = new ArrayList<Node>();
		for (Node node : nodePath1) {
			nodePathConcat.add(node);
		}
		for (Node node : nodePath2) {
			nodePathConcat.add(node);
		}
		for (Node node : nodePath3) {
			nodePathConcat.add(node);
		}
		return nodePathConcat;
	}
	
	/** Determines whether the given path is alternating in that:
	 *  (1) it begins with an edge not in the matching
	 *  (2) it ends with an edge not in the matching
	 *  (3) consecutive edges switch between being in and not in the matching
	 */
	protected static boolean isValidAlternatingPath(ArrayList<Edge> path, Matching matching) {
		if (path == null || matching == null) return false;
		if (path.size() % 2 == 0) return false; //must have odd number of edges, not even
		
		//these edges should all NOT be there in the matching
		for (int i = 0; i < path.size(); i += 2) {
			Edge edge = path.get(i);
			boolean there = matching.hasEdge(edge);
			if (there) return false;
		}
		
		//these edges should all BE there in the matching
		for (int i = 1; i < path.size(); i += 2) {
			Edge edge = path.get(i);
			boolean there = matching.hasEdge(edge);
			if (!there) return false;
		}
		
		return true;
	}
}
