package matchings;

public class TreeEdge {
	
	protected final TreeNode node1;
	protected final TreeNode node2;
	protected boolean marked;
	
	public TreeEdge(TreeNode node1, TreeNode node2) {
		this.node1 = node1;
		this.node2 = node2;
		marked = false;
	}
	
	public TreeNode getNode1() {
		return node1;
	}
	
	public TreeNode getNode2() {
		return node2;
	}
	
	public TreeNode getOther(TreeNode node) {
		if (node1 == node) return node2;
		else return node1;
	}
	
	public boolean containsNode(TreeNode node) {
		return node1 == node || node2 == node;
	}
	
	@Override
	public String toString() {
		return node1 + " <-> " + node2;
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
