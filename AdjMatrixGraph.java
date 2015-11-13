import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class AdjMatrixGraph extends Graph{
	
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
	
	public List<Integer> getNeighbors(int id) {
		List<Integer> neighbors = new ArrayList<Integer>();
		
		for (int i = 0; i < nodes; i++ ) {
			if (adjMatrix[id][i] != 0) {
				neighbors.add(i);
			}
		}
		
		return neighbors;	
	}
	
	
	public List<Integer> getInboundNodes(int id) {
		List<Integer> neighbors = new ArrayList<Integer>();
		
		for (int i = 0; i < nodes; i++ ) {
			if (adjMatrix[i][id] != 0) {
				neighbors.add(i);
			}
		}
		
		return neighbors;	
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

	@Override
	public Node getNode(int i) {
		return new Node(i) {
			@Override
			void addEdge(Node a) {
				AdjMatrixGraph.this.addEdge(this, a);
			}

			@Override
			Set<Node> getAdjSet() {
				return
					getAdjStream()
						.collect(Collectors.toCollection(HashSet::new));
			}

			@Override
			Stream<Node> getAdjStream() {
				return
					IntStream.range(0, nodes).filter(j -> adjMatrix[i][j] != 0)
						.mapToObj(AdjMatrixGraph.this::getNode);
			}
		};
	}
	


}
