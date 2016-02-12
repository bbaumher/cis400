package graph;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class CustomizableNode<T> extends Node<T> {
	
	private Set<Node<T>> neighbors;

	public CustomizableNode(T id) {
		super(id);
		neighbors = new HashSet<>();
	}

	void addEdge(CustomizableNode<T> n) {
		this.neighbors.add(n);
		n.neighbors.add(this);
	}

	@Override
	Set<Node<T>> getAdjSet() {
		return new HashSet<>(neighbors);
	}

	@Override
	Stream<Node<T>> getAdjStream() {
		return neighbors.stream();
	}

	@Override @Deprecated
	void addEdge(Node<T> a) {

	}

}
