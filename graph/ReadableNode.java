package graph;

import java.util.Set;
import java.util.stream.Stream;

public abstract class ReadableNode<T> {
  final T id;

  ReadableNode(T id) {
		this.id = id;
	}

	public T getId() {
		return id;
	}
  
  abstract Set<? extends ReadableNode<T>> getAdjSet();

	abstract Stream<? extends ReadableNode<T>> getAdjStream();

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ReadableNode)) {
			return false;
		}
		ReadableNode<T> node = (ReadableNode) obj;
		return node.id == id;
	}


	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return id + "";
	}
}
