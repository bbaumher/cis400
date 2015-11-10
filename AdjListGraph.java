import java.util.ArrayList;
import java.util.List;

public class AdjListGraph implements Graph { // undirected graph
	
	int nodes;
	int nodeCnt;
	List<Node> nodeList;
	
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
			for (Node j : n.getAdj()) {
				System.out.println("EDGE: " + n.getId() + ", " + j.getId());
			}
		}
		
	}
}
