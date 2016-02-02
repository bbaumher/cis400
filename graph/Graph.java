package graph;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

public abstract class Graph<T> extends ReadableGraph<T> {
  public abstract Node<T> addNode();
	
	public abstract void addEdge(Node<T> i, Node<T> j);

	public abstract void addEdge(T i, T j) ;

  @Override
	public Set<Node<T>> getNeighbors(ReadableNode<T> n) {
		return (Set<Node<T>>) super.getNeighbors(n);
	}

  @Override
	public abstract Node<T> getNode(T i);

  @Override
	public abstract Stream<Node<T>> getNodes();

  @Override
	public Iterator<Node<T>> getDFSIterator() {
		return (Iterator<Node<T>>) super.getDFSIterator();
	}

  @Override
	public Iterator<Node<T>> getDFSIterator(ReadableNode<T> s) {
		return (Iterator<Node<T>>) super.getDFSIterator(s);
	}
}
