
import java.util.Arrays;
import java.util.Iterator;

public class Tester {
	
	/** Run the algorithm on a test graph.
	 */
	public static void main(String[] args) {
		GraphGenerator gg = new StandardGraphGenerator();
		Graph testGraph = gg.generateAdjListGraph(1000, 0.010);
		int k = 4;
		
		//System.out.println(testGraph);
		//System.out.println(SCCTester.isStronglyConnected(testGraph));
		for (int i = 1; i <= 100; i++) {
			int coverTime = NodeCoverRunner.getCoverTime(testGraph, testGraph.getNode(0), k);
			System.out.println(i + "\t" + coverTime);
		}
	}
	
	/** Run the algorithm on a test graph.
	 */
	public static void main2(String[] args) {
		//create nodes
		int s = 0;
		int a = 1;
		int b = 2;
		int c = 3;
		int d = 4;
		int e = 5;
		int f = 6;
		int g = 7;
		int h = 8;
		int i = 9;
		int j = 10;
		int k = 11;
		
		//create graph
		AdjListGraph testGraph = new AdjListGraph(12);
		
		//add edges to the graph
		testGraph.addEdge(s,a);
		testGraph.addEdge(s,b);
		testGraph.addEdge(s,c);
		testGraph.addEdge(s,d);
		testGraph.addEdge(a,e);
		testGraph.addEdge(a,g);
		testGraph.addEdge(b,e);
		testGraph.addEdge(b,f);
		testGraph.addEdge(c,d);
		testGraph.addEdge(c,h);
		testGraph.addEdge(c,i);
		testGraph.addEdge(d,h);
		testGraph.addEdge(e,j);
		testGraph.addEdge(g,j);
		testGraph.addEdge(i,k);
		
		Node sNode = testGraph.getNode(s);
		Iterator<Node> iter = testGraph.getDFSIterator(sNode);
		
		while (iter.hasNext()) {
			Node node = iter.next();
			System.out.println(node);
		}
		
		Graph graph =
			new StandardGraphGenerator().generateAdjListGraph(20, 0.2);
		graph.printGraph();
		Graph largestComponent = null;
		Iterator<Graph> iterator =
			SCCTester.getStronglyConnectedComponents(graph);
		while (iterator.hasNext()) {
			Graph component = iterator.next();
			if (
				largestComponent == null
					|| largestComponent.getNodeCnt()
						< component.getNodeCnt())
			{
				largestComponent = component;
			}
		}
		
		largestComponent.printGraph();
		
		//run the algorithm
		TransitionMatrix transitionMatrix =
			ProbabilityDistributionAlgorithm
				.getTransitionMatrix(largestComponent, 3);
		
		//display the output
		print(transitionMatrix.getTransitionVectors());
		
		System.out.println(
			ConvergenceTester.forTransitionMatrix(transitionMatrix)
				.logStepsForConvergence(0.125));
		
		ConvergenceTester convergenceTester =
			ConvergenceTester.forTransitionMatrix(transitionMatrix);
			
		int iteration = 1;
		while (true) {
			convergenceTester.iterateDistributions(1);
			System.out.print("2^");
			System.out.print(iteration++);
			System.out.print(" iterations; distance ");
			System.out.println(convergenceTester.convergenceDistance());
			print(convergenceTester.getTransitionMatrix().getTransitionVectors()
			);
		}
	}
	
	private static void print(double[][] transitionVectors) {
		Arrays.stream(transitionVectors)
			.map(
				transitionVector -> {
					Iterable<String> iterable =
						Arrays.stream(transitionVector)
								.mapToObj(Double::toString)
							::iterator;
					return String.join(" ", iterable);
				}
			)
			.forEachOrdered(System.out::println);
		System.out.println();
	}
	
}
