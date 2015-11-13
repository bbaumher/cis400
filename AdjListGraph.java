import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AdjListGraph extends Graph { // undirected graph
	
	int nodes;
	int nodeCnt;
	ArrayList<Node> nodeList;
	
	public AdjListGraph(int n) {
		nodes = n;
		nodeCnt = 0;
		nodeList = new ArrayList<Node>();
		for (int i = 0; i < n; i++) {
			nodeList.add(new AdjListNode(i));
		}
	}

	@Override
	public int getNodeCnt() {
		return nodes;
	}
	
	@Override
	public Node addNode() {
		Node node = new AdjListNode(nodeCnt);
		nodeCnt++;
		nodeList.add(node);
		return node;
	}
	
	
	public void addEdge(int i, int j) throws IllegalArgumentException {
		if (i >= nodes || j >= nodes) {
			throw new IllegalArgumentException("bad index");
		}
		nodeList.get(i).addEdge(nodeList.get(j));	
	}
	
	 
	
	public void addEdge(Node i, Node j) {
		i.addEdge(j);
	}
	
	@Override
	public Node getNode(int index) {
		return nodeList.get(index);
	}

	@Override
	public List<Integer> getNeighbors(int id) {
		Node node = this.getNode(id);
		Iterable<Node> neighbors = node.getAdjStream()::iterator;
		List<Integer> neighborIds = new ArrayList<Integer>();
		for ( Node n : neighbors ) {
			neighborIds.add(n.getId());	
		}
		return neighborIds;
	}

	@Override
	public List<Integer> getInboundNodes(int id) {
		List<Integer> neighbors = new ArrayList<Integer>();
		Node node = this.getNode(id);
		for (Node n : nodeList) {
			if (n.getAdjSet().contains(node)) {
				neighbors.add(n.getId());
			}
		}
		return neighbors;
	}
	
	public List<Node> getInboundNodes(Node node) {
		List<Node> neighbors = new ArrayList<Node>();
		
		for (Node n : nodeList) {
			if (n.getAdjSet().contains(node)) {
				neighbors.add(n);
			}
		}
		return neighbors;
	}

	@Override
	public Stream<Node> getNodes() {
		return IntStream.range(0, getNodeCnt()).mapToObj(this::getNode);
	}
	
	private static final class AdjListNode extends Node {
		private Set<Node> adjSet;

		public AdjListNode (int id) {
			super(id);
			this.adjSet = new HashSet<>();
		}

		@Override
		public void addEdge(Node a) {
			adjSet.add(a);
		}

		@Override
		public Set<Node> getAdjSet() {
			return adjSet;
		}

		@Override
		public Stream<Node> getAdjStream() {
			return getAdjSet().stream();
		}
	}
}
