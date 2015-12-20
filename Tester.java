
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class Tester {
	private static final Random RANDOM = new Random();
	
	public static void main(String[] args) {
		testZeroWeights();
	}
	
	/** Run the algorithm on a test graph.
	 */
	public static void main4(String[] args) {
		GraphGenerator gg = new StandardGraphGenerator(RANDOM);
		Graph testGraph;
		int k = 4;
		
		//System.out.println(testGraph);
		//System.out.println(SCCTester.isStronglyConnected(testGraph));
		for (int i = 1; i <= 100; i++) {
			testGraph = gg.generateAdjListGraph(1000, 0.010);
			int coverTime =
				new NodeCoverRunner(RANDOM)
					.getCoverTime(testGraph, testGraph.getNode(0), k);
			System.out.println(i + "\t" + coverTime);
		}
	}
	
	public static void main3(String[] args) {
		//create nodes
		int s = 0;
		int a = 1;
		int b = 2;
		int c = 3;
		
		//create graph
		AdjListGraph testGraph = new AdjListGraph(4);
		
		//add edges to the graph
		testGraph.addEdge(s,a);
		testGraph.addEdge(a,b);
		testGraph.addEdge(b,c);
		testGraph.addEdge(c,s);
		
		int counter = 0;
		Iterator<ReadableGraph> iter =
      SCCTester.getStronglyConnectedComponents(testGraph);
		while (iter.hasNext()) {
			ReadableGraph g = iter.next();
			System.out.println("graph " + counter + ": ");
			g.printGraph();
			counter++;
		}
		System.out.println(iter + " graphs in total");
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
			new StandardGraphGenerator(RANDOM).generateAdjListGraph(20, 0.2);
		graph.printGraph();
		ReadableGraph largestComponent =
      getLargestStronglyConnectedComponent(graph);
		
		largestComponent.printGraph();
		
		//run the algorithm
		TransitionMatrix transitionMatrix =
			ProbabilityDistributionAlgorithm
				.getTransitionMatrix(
					largestComponent,
					3,
					ProbabilityDistributionAlgorithm::calculateCredits);
		
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
	
	/**
	 * Return a {@link Graph} representing the largest strongly-connected
	 * component of the specified {@link Graph}.
	 * 
	 * @param graph The original graph.
	 * @return {@code graph}'s largest strongly connected component.
	 */
	private static ReadableGraph getLargestStronglyConnectedComponent(Graph graph)
  {
		Iterable<ReadableGraph> iterable =
			() -> SCCTester.getStronglyConnectedComponents(graph);
		return
			StreamSupport.stream(iterable.spliterator(), false)
				.sorted(
					(component1, component2) ->
						Integer
							.compare(
								component1.getNodeCnt(),
								component2.getNodeCnt()))
				.findFirst()
				.get();
	}
	
	private static void testZeroWeights() {
		Graph graph = new AdjListGraph(4);
		graph.addEdge(0, 1);
		graph.addEdge(0, 2);
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);
		graph.addEdge(3, 0);
		TransitionMatrix transitionMatrix =
			ProbabilityDistributionAlgorithm
				.getTransitionMatrix(
					graph,
					2,
					ProbabilityDistributionAlgorithm::calculateCredits);
		print(transitionMatrix.getTransitionVectors());
	}
	
	private static void testLollipop() {
		int nodeCount = 10;
		int k = 2;
		Graph graph = LollipopGraphGenerator.generateAdjListGraph(nodeCount);
		TransitionMatrix transitionMatrix =
			ProbabilityDistributionAlgorithm
				.getTransitionMatrix(
					graph,
					k,
					ProbabilityDistributionAlgorithm::calculateCredits);
		print(transitionMatrix.getTransitionVectors());
	}
	
	private static void compareAlgorithms() {
		int nodeCount = 1000;
		int logSize = 32 - Integer.numberOfLeadingZeros(nodeCount);
//		ReadableGraph graph =
//			getLargestStronglyConnectedComponent(
//				new StandardGraphGenerator(RANDOM)
//					.generateAdjListGraph(
//						nodeCount,
//						logSize / (float) nodeCount));
		Graph graph = LollipopGraphGenerator.generateAdjListGraph(nodeCount);
		System.out.println(graph.getNodeCnt());
		List<ConvergenceTester> testers = new ArrayList<>(4);
		testers.add(
			ConvergenceTester.forTransitionMatrix(
				UniformDistributionAlgorithm.getTransitionMatrix(graph)));
		Consumer<ProbabilityDistributionAlgorithm.CreditCalculator> addTester =
			calculator ->
				testers.add(
					ConvergenceTester.forTransitionMatrix(
						ProbabilityDistributionAlgorithm
							.getTransitionMatrix(graph, logSize, calculator)));
		addTester.accept(ProbabilityDistributionAlgorithm::calculateCredits);
		addTester.accept(ProbabilityDistributionAlgorithm::calculateCredits2);
		addTester.accept(ProbabilityDistributionAlgorithm::calculateCredits3);
		System.out.println(
			"Log steps;"
				+ "Standard random walk;"
				+ "Exactly k away;"
				+ "Up to k away;"
				+ "Up to k away, proportional to distance");
		int logSteps = 0;
		while (true) {
			System.out.print(logSteps++);
			for (ConvergenceTester tester : testers) {
				if (logSteps > 1) {
					tester.iterateDistributions(1);
				}
				System.out.print(';');
				System.out.print(tester.convergenceDistance());
			}
			System.out.println();
		}
	}
}
