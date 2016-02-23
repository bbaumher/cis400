package matchings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

import graph.CustomizableGraph;
import graph.CustomizableNode;
import graph.Node;

public class MatchingGraphGenerator {
	
	public static MatchingGraph generate(Graph graph) {
		
		//get an arbitrary matching of the graph
		Matching arbitraryMatching = new Matching(graph);
		arbitraryMatching.fill();
		
		//create the matching graph and its first node
		MatchingGraph matchingGraph = new MatchingGraph();
		MatchingNode arbitraryMatchingNode = new MatchingNode(arbitraryMatching);
		matchingGraph.addNode(arbitraryMatchingNode);
		
		//these will help do a BFS to create the matching graph
		Queue<MatchingNode> queueMatchings = new LinkedList<MatchingNode>();
		Map<Matching,MatchingNode> mapMatchings = new TreeMap<Matching,MatchingNode>();
		queueMatchings.add(arbitraryMatchingNode);
		mapMatchings.put(arbitraryMatching, arbitraryMatchingNode);
		
		while (!queueMatchings.isEmpty()) {
			//dequeue the matching node and extract relevant fields
			MatchingNode dequeuedMatchingNode = queueMatchings.poll();
			Matching dequeuedMatching = dequeuedMatchingNode.getMatching();
			ArrayList<Matching> neighborMatchings = dequeuedMatching.getNeighborMatchings();
			
			for (Matching neighborMatching : neighborMatchings) {
				if (!mapMatchings.containsKey(neighborMatching)) { //haven't seen this matching before
					MatchingNode neighborMatchingNode = new MatchingNode(neighborMatching);
					matchingGraph.addNode(neighborMatchingNode);
					matchingGraph.addEdgeBetweenNodes(dequeuedMatchingNode, neighborMatchingNode);
					
					mapMatchings.put(neighborMatching, neighborMatchingNode);
					queueMatchings.add(neighborMatchingNode);
				}
				else { //have seen this matching before
					MatchingNode neighborMatchingNode = mapMatchings.get(neighborMatching);
					matchingGraph.addEdgeBetweenNodes(dequeuedMatchingNode, neighborMatchingNode);
				}
			}
		}
	
		return matchingGraph;
	}
	
public static CustomizableGraph<Matching> generate2(Graph graph) {
		
		//get an arbitrary matching of the graph
		Matching arbitraryMatching = new Matching(graph);
		arbitraryMatching.fill();
		
		//create the matching graph and its first node
		CustomizableGraph<Matching> matchingGraph = new CustomizableGraph<Matching>();
		CustomizableNode<Matching> arbitraryMatchingNode = matchingGraph.addNode(arbitraryMatching);
		
		//these will help do a BFS to create the matching graph
		Queue<CustomizableNode<Matching>> queueMatchings = new LinkedList<CustomizableNode<Matching>>();
		Map<Matching,CustomizableNode<Matching>> mapMatchings = new TreeMap<Matching,CustomizableNode<Matching>>();
		queueMatchings.add(arbitraryMatchingNode);
		mapMatchings.put(arbitraryMatching, arbitraryMatchingNode);
		
		while (!queueMatchings.isEmpty()) {
			//dequeue the matching node and extract relevant fields
			CustomizableNode<Matching> dequeuedMatchingNode = queueMatchings.poll();
			Matching dequeuedMatching = dequeuedMatchingNode.getId();
			ArrayList<Matching> neighborMatchings = dequeuedMatching.getNeighborMatchings();
			
			for (Matching neighborMatching : neighborMatchings) {
				if (!mapMatchings.containsKey(neighborMatching)) { //haven't seen this matching before
					CustomizableNode<Matching> neighborMatchingNode = matchingGraph.addNode(neighborMatching);
					matchingGraph.addUndirectedEdge(dequeuedMatchingNode.getId(), neighborMatchingNode.getId());
					
					mapMatchings.put(neighborMatching, neighborMatchingNode);
					queueMatchings.add(neighborMatchingNode);
				}
				else { //have seen this matching before
					CustomizableNode<Matching> neighborMatchingNode = mapMatchings.get(neighborMatching);
					matchingGraph.addUndirectedEdge(dequeuedMatchingNode.getId(), neighborMatchingNode.getId());
				}
			}
		}
	
		return matchingGraph;
	}
	
	public static graph.Graph<Matching> generate(
    graph.ReadableGraph<Integer> graph)
  {
		Graph newGraph = new Graph(graph.getNodeCnt());
		
		Stream<? extends graph.ReadableNode<Integer>> stream = graph.getNodes();
		Iterator<? extends graph.ReadableNode<Integer>> iter = stream.iterator();
		while (iter.hasNext()) {
			graph.ReadableNode<Integer> node = iter.next();
			Set<? extends graph.ReadableNode<Integer>> neighbors =
        graph.getNeighbors(node);
			int n = node.getId();
			for (graph.ReadableNode<Integer> neighbor : neighbors) {
				int m = neighbor.getId();
				if (n < m) {
					matchings.Node node1 = newGraph.getNodeIndexedAt(n);
					matchings.Node node2 = newGraph.getNodeIndexedAt(m);
					newGraph.addEdgeBetweenNodes(node1, node2);
				}
			}
		}
		
		return generate2(newGraph);
		
	}

}
