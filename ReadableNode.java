
import java.util.Set;
import java.util.stream.Stream;

public abstract class ReadableNode {
  final int id;

  ReadableNode(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
  
  abstract Set<? extends ReadableNode> getAdjSet();

	abstract Stream<? extends ReadableNode> getAdjStream();

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ReadableNode)) {
			return false;
		}
		ReadableNode node = (ReadableNode) obj;
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
