package graph;

public class Edge<T> {// for directed graphs if we do that
	
	private final ReadableNode<T> start;
	private final ReadableNode<T> end;
	
	public Edge(ReadableNode<T> a, ReadableNode<T> b) {
		start = a;
		end = b;
	}

	public ReadableNode<T> getStart() {
		return start;
	}
	
	public ReadableNode<T> getEnd() {
		return end;
	}

	
}
