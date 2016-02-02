package matchings;

public class Edge {
	
	private final Node node1;
	private final Node node2;
	
	public Edge(Node node1, Node node2) {
		this.node1 = node1;
		this.node2 = node2;
	}
	
	public Node getNode1() {
		return node1;
	}
	
	public Node getNode2() {
		return node2;
	}
	
	public Node getOther(Node node) {
		if (node1 == node) return node2;
		else return node1;
	}
	
	@Override
	public String toString() {
		return node1 + " <-> " + node2;
	}

}
