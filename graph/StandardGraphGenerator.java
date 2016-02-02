package graph;

import java.util.Random;


public class StandardGraphGenerator implements GraphGenerator {
	private final Random random;
	
	public StandardGraphGenerator(Random random) {
		this.random = random;
	}

	@Override
	public AdjMatrixGraph generateAdjMatrixGraph(int nodes, double p) {
		AdjMatrixGraph g = new AdjMatrixGraph(nodes);
		fillInEdges(g, p);
		return g;
	}
	
	@Override
	public AdjListGraph generateAdjListGraph(int nodes, double p) {
		AdjListGraph g = new AdjListGraph(nodes);
		fillInEdges(g, p);
		return g;
	}
	
	/** Randomly fills in the edges of g,
	 *  each edge appearing with probability p.
	 *  No self loops are allowed.
	 */
	private void fillInEdges (Graph<Integer> g, double p) {
		
		for (int i = 0; i < g.getNodeCnt(); i++) { 
			int edgesAdded = 0;
			
			//ensure that every node has at least one outgoing edge by continuing to loop
			for (int j = 0; j < g.getNodeCnt() || edgesAdded == 0; j++) { // directed graphs!!
				j %= g.getNodeCnt();
				if (i == j) {
					continue;  // no self loops, can change this
				}
				double d = random.nextDouble();
				if (d < p) {
					try {
						g.addEdge(i,j);
						edgesAdded++;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		


	}

}
