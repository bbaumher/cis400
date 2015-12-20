import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

public abstract class Graph extends ReadableGraph {
  public abstract Node addNode();
	
	public abstract void addEdge(Node i, Node j);

	public abstract void addEdge(int i, int j) ;

  @Override
	public Set<Node> getNeighbors(ReadableNode n) {
		return (Set<Node>) super.getNeighbors(n);
	}

  @Override
	public abstract Node getNode(int i);

  @Override
	public abstract Stream<Node> getNodes();

  @Override
	public Iterator<Node> getDFSIterator() {
		return (Iterator<Node>) super.getDFSIterator();
	}

  @Override
	public Iterator<Node> getDFSIterator(ReadableNode s) {
		return (Iterator<Node>) super.getDFSIterator(s);
	}
}
