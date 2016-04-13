package matchings;
import java.util.ArrayList;

public class TreeNode implements Comparable<Node> {

	protected final ArrayList<TreeEdge> edgeList;
	protected final ArrayList<TreeNode> neighbors;
	protected final String name;
	protected boolean marked;
	
	public TreeNode (Node node) {
		edgeList = new ArrayList<TreeEdge>();
		neighbors = new ArrayList<TreeNode>();
		this.name = node.name;
		marked = false;
	}
	
	public boolean hasEdgeTo(TreeNode other) {
		for (TreeEdge edge : edgeList) {
			TreeNode other2 = edge.getOther(this);
			if (other == other2) return true;
		}
		return false;
	}
	
	public TreeEdge addEdgeTo(TreeNode other) {
		if (this.hasEdgeTo(other)) return null; //don't add edge that already exists
		TreeEdge edge = new TreeEdge(this, other);
		this.edgeList.add(edge);
		other.edgeList.add(edge);
		this.neighbors.add(other);
		other.neighbors.add(this);
		return edge;
	}
	
	public TreeEdge getEdgeTo(TreeNode other) {
		for (TreeEdge edge : edgeList) {
			if (edge.getOther(this) == other) {
				return edge;
			}
		}
		return null;
	}
	
	public ArrayList<TreeEdge> getEdgeList() {
		return edgeList;
	}
	
	public ArrayList<TreeNode> getNeighbors() {
		return neighbors;
	}
	
	@Override
	public String toString() {
		return name;
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

	@Override
	public int compareTo(Node arg0) {
		return this.name.compareTo(arg0.name);
	}
	
}
