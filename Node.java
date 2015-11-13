import java.util.Set;
import java.util.stream.Stream;

public abstract class Node {
	
	final int id;
	
	public Node (int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	abstract void addEdge(Node a);
	
	abstract Set<Node> getAdjSet();

	abstract Stream<Node> getAdjStream();

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Node)) {
			return false;
		}
		Node node = (Node) obj;
		return node.id == id;
	}
	
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public String toString() {
		return id + "";
	}
	

}
