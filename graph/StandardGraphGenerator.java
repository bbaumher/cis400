package graph;

import java.util.Random;


public class StandardGraphGenerator implements GraphGenerator {
	private final Random random;
	
	public StandardGraphGenerator(Random random) {
		this.random = random;
	}

	@Override
	public AdjMatrixGraph generateAdjMatrixGraph(
    int nodes,
    double p,
    boolean directed)
  {
		AdjMatrixGraph g = new AdjMatrixGraph(nodes);
		fillInEdges(g, p, directed);
		return g;
	}
	
	@Override
	public AdjListGraph generateAdjListGraph(int nodes, double p, boolean directed
  ) {
		AdjListGraph g = new AdjListGraph(nodes);
		fillInEdges(g, p, directed);
		return g;
	}
	
	/** Randomly fills in the edges of g,
	 *  each edge appearing with probability p.
	 *  No self loops are allowed.
	 */
	private void fillInEdges (Graph<Integer> g, double p, boolean directed) {
		
		for (int i = 0; i < g.getNodeCnt(); i++) { 
			int edgesAdded = 0;
			
			for (int j = directed ? 0 : i + 1; j < g.getNodeCnt(); j++) {
				j %= g.getNodeCnt();
				if (i == j) {
					continue;  // no self loops, can change this
				}
				double d = random.nextDouble();
				if (d < p) {
					try {
						g.addEdge(i,j);
            if (!directed) {
              g.addEdge(j, i);
            }
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
