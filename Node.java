import java.util.Set;
import java.util.stream.Stream;

public abstract class Node extends ReadableNode {
	public Node (int id) {
		super(id);
	}
	
	abstract void addEdge(Node a);

  @Override
	abstract Set<Node> getAdjSet();

  @Override
	abstract Stream<Node> getAdjStream();
}
