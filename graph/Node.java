package graph;
import java.util.Set;
import java.util.stream.Stream;

public abstract class Node<T> extends ReadableNode<T> {
	public Node (T id) {
		super(id);
	}
	
	abstract void addEdge(Node<T> a);

  @Override
	abstract Set<Node<T>> getAdjSet();

  @Override
	abstract Stream<Node<T>> getAdjStream();
}
