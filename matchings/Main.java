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
		
		
		
		Graph graph = new Graph(8,'a');
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
