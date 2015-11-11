import java.util.ArrayList;
import java.util.List;

public class AdjListGraph implements Graph { // undirected graph
	
	int nodes;
	int nodeCnt;
	ArrayList<Node> nodeList;
	
	public AdjListGraph(int n) {
		nodes = n;
		nodeCnt = 0;
		nodeList = new ArrayList<Node>();
		for (int i = 0; i < n; i++) {
			nodeList.add(new Node(i));
		}
	}

	@Override
	public void search() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getNodeCnt() {
		return nodes;
	}
	
	@Override
	public void addNode() {
		Node node = new Node(nodeCnt);
		nodeCnt++;
		nodeList.add(node);
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

	
	public void printGraph() {
		System.out.println("Node size: " + getNodeCnt());
		
		for (Node n : nodeList) { // no self loops
			System.out.print(n.getId() + ": ");
			boolean printedAlready = false;
			for (Node j : n.getAdj()) {
				if (printedAlready) {
					System.out.print(", ");
				}
				System.out.print(j.getId());
				printedAlready = true;
			}
			System.out.println();
		}
		
	}
	
	public Node getNode(int index) {
		return nodeList.get(index);
	}

	@Override
	public List<Integer> getNeighbors(int node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Node> getNeighbors(Node n) {
		return n.getAdj();
	}

	@Override
	public List<Integer> getInboundNodes(int id) {
		List<Integer> neighbors = new ArrayList<Integer>();
		Node node = this.getNode(id);
		for (Node n : nodeList) {
			if (n.getAdj().contains(node)) {
				neighbors.add(n.getId());
			}
		}
		return neighbors;
	}
	
	public List<Node> getInboundNodes(Node node) {
		List<Node> neighbors = new ArrayList<Node>();
		
		for (Node n : nodeList) {
			if (n.getAdj().contains(node)) {
				neighbors.add(n);
			}
		}
		return neighbors;
	}
}
