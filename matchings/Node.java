package matchings;
import java.util.ArrayList;

public class Node implements Comparable<Node> {

	protected final ArrayList<Edge> edgeList;
	protected final ArrayList<Node> neighbors;
	protected final String name;
	protected boolean marked;
	
	public Node (String name) {
		edgeList = new ArrayList<Edge>();
		neighbors = new ArrayList<Node>();
		this.name = name;
		marked = false;
	}
	
	public boolean hasEdgeTo(Node other) {
		for (Edge edge : edgeList) {
			Node other2 = edge.getOther(this);
			if (other == other2) return true;
		}
		return false;
	}
	
	public Edge addEdgeTo(Node other) {
		if (this.hasEdgeTo(other)) return null; //don't add edge that already exists
		Edge edge = new Edge(this, other);
		this.edgeList.add(edge);
		other.edgeList.add(edge);
		this.neighbors.add(other);
		other.neighbors.add(this);
		return edge;
	}
	
	public Edge getEdgeTo(Node other) {
		for (Edge edge : edgeList) {
			if (edge.getOther(this) == other) {
				return edge;
			}
		}
		return null;
	}
	
	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}
	
	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}
	
	@Override
	public String toString() {
		String string = name;
		if (marked) string += "'";
		return string;
	}

	@Override
	public int compareTo(Node other) {
		if(this instanceof MatchingNode) {
			MatchingNode matchingThis = (MatchingNode)this;
			MatchingNode matchingOther = (MatchingNode)other;
			return matchingThis.getMatching().compareTo(matchingOther.getMatching());
		}
		
		return this.toString().compareTo(other.toString());
	}
	
	public boolean isMarked() {
		return marked;
	}
	
	public void mark() {
		marked = true;
	}
	
	public void unmark() {
		marked = false;
	}
	
}
