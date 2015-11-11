
import java.util.Arrays;

public class Tester {
	
	/** Run the algorithm on a test graph.
	 */
	public static void main(String[] args) {
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
		
		//run the algorithm
		double[][] transitionVectors =
			ProbabilityDistributionAlgorithm.run(testGraph, 3);
		
		//display the output
		print(transitionVectors);
		
		ConvergenceTester convergenceTester =
			ConvergenceTester.forTransitionMatrix(transitionVectors);
			
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
