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
		
		
		
		Graph graph = new Graph(8);
		
		
		
		Node n0 = graph.getNodeIndexedAt(0);
		Node n1 = graph.getNodeIndexedAt(1);
		Node n2 = graph.getNodeIndexedAt(2);
		Node n3 = graph.getNodeIndexedAt(3);
		Node n4 = graph.getNodeIndexedAt(4);
		Node n5 = graph.getNodeIndexedAt(5);
		Node n6 = graph.getNodeIndexedAt(6);
		Node n7 = graph.getNodeIndexedAt(7);
		
		//graph.addEdgeBetweenNodes(n0,n1);
		graph.addEdgeBetweenNodes(n0,n4);
		//graph.addEdgeBetweenNodes(n0,n5);
		graph.addEdgeBetweenNodes(n1,n2);
		graph.addEdgeBetweenNodes(n1,n4);
		graph.addEdgeBetweenNodes(n1,n5);
		graph.addEdgeBetweenNodes(n1,n6);
		graph.addEdgeBetweenNodes(n2,n3);
		graph.addEdgeBetweenNodes(n2,n5);
		graph.addEdgeBetweenNodes(n2,n6);
		graph.addEdgeBetweenNodes(n2,n7);
		graph.addEdgeBetweenNodes(n3,n6);
		graph.addEdgeBetweenNodes(n3,n7);
		graph.addEdgeBetweenNodes(n4,n5);
		graph.addEdgeBetweenNodes(n5,n6);
		graph.addEdgeBetweenNodes(n6,n7);
		
		Matching matching = new Matching(graph);
		matching.fillSmartly();
		System.out.println(matching);
		
		/*
		
		MatchingGraph matchingGraph = MatchingGraphGenerator.generate(graph);
		
		GraphAssessor.assess(graph);
		System.out.println();
		GraphAssessor.assess(matchingGraph);
		*/
		
		
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
		
		/*
		graph.AdjListGraph graph = new graph.AdjListGraph(4);
		graph.addEdge(0,1);
		graph.addEdge(1,2);
		graph.addEdge(2,3);
		graph.addEdge(3,0);
		graph.printGraph();
		
		
		Graph<Matching> matchingGraph = MatchingGraphGenerator.generate(graph);
		matchingGraph.printGraph();*/
	}

}
