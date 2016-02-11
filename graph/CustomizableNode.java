package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class CustomizableNode<T> extends Node<T> {
	
	private ArrayList<Node<T>> neighbors;

	public CustomizableNode(T id) {
		super(id);
		neighbors = new ArrayList<Node<T>>();
	}

	void addEdge(CustomizableNode<T> n) {
		this.neighbors.add(n);
		n.neighbors.add(this);
	}

	@Override
	Set<Node<T>> getAdjSet() {
		Set<Node<T>> set = new HashSet<Node<T>>();
		for (Node<T> n : neighbors) {
			set.add(n);
		}
		return set;
	}

	@Override @Deprecated
	Stream<Node<T>> getAdjStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override @Deprecated
	void addEdge(Node<T> a) {

	}

}
