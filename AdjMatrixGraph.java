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
	public int getNodeCnt() {
		return nodes;
	}
	
	// shouldn't be able to add a node, would have to change array size....
	@Override
	public Node addNode() {
		return null;
	}

  @Override
	public void addEdge(int i, int j) {
		adjMatrix[i][j] = 1;
	}
	
	/**
	 * @param Node i,j: both node ends of edge, undirected, so doesnt matter  
	 * 
	 */
  @Override
	public void addEdge(Node i, Node j) throws IllegalArgumentException {
		if (i.getId() >= nodes || j.getId() >= nodes) {
			throw new IllegalArgumentException("bad index");
		}
		adjMatrix[i.getId()][j.getId()] = 1;
	}

  @Override
	public List<Integer> getNeighbors(int id) {
		List<Integer> neighbors = new ArrayList<>();
		
		for (int i = 0; i < nodes; i++ ) {
			if (adjMatrix[id][i] != 0) {
				neighbors.add(i);
			}
		}
		
		return neighbors;	
	}
	
	@Override
	public List<Integer> getInboundNodes(int id) {
		List<Integer> neighbors = new ArrayList<>();
		
		for (int i = 0; i < nodes; i++ ) {
			if (adjMatrix[i][id] != 0) {
				neighbors.add(i);
			}
		}
		
		return neighbors;	
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

	@Override
	public Stream<Node> getNodes() {
		return IntStream.range(0, getNodeCnt()).mapToObj(this::getNode);
	}
	


}
