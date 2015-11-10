
public class StandardGraphGenerator implements GraphGenerator {
	
	public StandardGraphGenerator() {
		
	}

	@Override
	public Graph generate(int nodes, double p) {
		
		Graph g = new AdjMatrixGraph(nodes);
		
		
		for (int i = 0; i < nodes; i++) { 
			for (int j = 0; j < nodes; j++) { // directed graphs!!
				if (i == j) {
					continue;  // no self loops, can change this
				}
				double d = Math.random();
				if (d <= p) {
					try {
						g.addEdge(i,j);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return g;

	}

	
	public static void main(String[] args) {
		GraphGenerator gg = new StandardGraphGenerator();
		Graph graph = gg.generate(10, .5);
		
		graph.printGraph();
		System.out.println();
		
		
	Graph graph2 = gg.generate(100, .3);
		
		graph2.printGraph();
		System.out.println();
	}
}
