package matchings;

public class Edge {
	
	protected final Node node1;
	protected final Node node2;
	protected boolean marked;
	
	public Edge(Node node1, Node node2) {
		this.node1 = node1;
		this.node2 = node2;
		marked = false;
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
		String string = node1 + " <-> " + node2;
		if (marked) string += "'";
		return string;
	}
	
	public boolean isMarked() {
		return marked;
	}
	
	public void mark() {
		marked = true;
	}
	
	public void unmark() {
		marked = false;
	}

}
