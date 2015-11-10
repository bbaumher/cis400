
public class Edge {// for directed graphs if we do that
	
	private Node start;
	private Node end;
	
	public Edge(Node a, Node b) {
		start = a;
		end = b;
	}

	public Node getStart() {
		return start;
	}
	
	public Node getEnd() {
		return end;
	}

	
}
