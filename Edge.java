
public class Edge<T> {// for directed graphs if we do that
	
	private final Node<T> start;
	private final Node<T> end;
	
	public Edge(Node<T> a, Node<T> b) {
		start = a;
		end = b;
	}

	public Node<T> getStart() {
		return start;
	}
	
	public Node<T> getEnd() {
		return end;
	}

	
}
