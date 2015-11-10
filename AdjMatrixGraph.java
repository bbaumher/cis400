
public class AdjMatrixGraph implements Graph{
	
	int[][] adjMatrix;
	int nodes;
	int nodeCnt;
	
	public AdjMatrixGraph(int n) {
		adjMatrix = new int[n][n];
		nodes = n;
		nodeCnt = 0;
	}

	@Override
	public void search() {
		// TODO Auto-generated method stub
	}

	@Override
	public int getNodeCnt() {
		return nodes;
	}
	
	// shouldn't be able to add a node, would have to change array size....
	@Override
	public void addNode() {
		Node node = new Node(nodeCnt);
		nodeCnt++;
	}
	
	public void addEdge(int i, int j) {
		adjMatrix[i][j] = 1;
	}
	
	/**
	 * @param Node i,j: both node ends of edge, undirected, so doesnt matter  
	 * 
	 */
	public void addEdge(Node i, Node j) throws IllegalArgumentException {
		if (i.getId() >= nodes || j.getId() >= nodes) {
			throw new IllegalArgumentException("bad index");
		}
		adjMatrix[i.getId()][j.getId()] = 1;
	}
	
	public void printGraph() {
		System.out.println("Node size: " + getNodeCnt());
		
		for (int i = 0; i < nodes; i++) { // no self loops
			for (int j = i+1; j < nodes; j++) { // only do probability one way for nodes
				if (adjMatrix[i][j] == 1) {
					System.out.println("EDGE: " + i + ", " + j);
				}
			}
		}
		
	}

}