
public class StandardGraphGenerator implements GraphGenerator {
	
	public StandardGraphGenerator() {
		
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
	private void fillInEdges (Graph g, double p) {
		
		for (int i = 0; i < g.getNodeCnt(); i++) { 
			for (int j = 0; j < g.getNodeCnt(); j++) { // directed graphs!!
				if (i == j) {
					continue;  // no self loops, can change this
				}
				double d = Math.random();
				if (d < p) {
					try {
						g.addEdge(i,j);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		


	}

}
