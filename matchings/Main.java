package matchings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {

	public static void main(String[] args) {
		//make the graph
		//Graph graph = new Graph(8, 'a');
		//Graph graph = new Graph(4, 'b');
		Graph graph = new Graph(12, 0.5);
		
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
		
		GraphAssessor.assess(graph);
		System.out.println();
		GraphAssessor.assess(matchingGraph);
		
		/*
		System.out.println(graph);
		
		System.out.println();
		*/
		/*
		for (Node node : matchingGraph.getNodeList()) {
			System.out.println(node);
			System.out.println();
		}
		*/
		//System.out.println(matchingGraph);
	}

}
