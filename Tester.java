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
		Node nodeS = testGraph.getNode(s);
		double[] p = ProbabilityDistributionAlgorithm.run(nodeS, 3);
		
		//display the output
		for (int z = 0; z < p.length; z++) {
			System.out.println(p[z]);
		}
	}
	
}
