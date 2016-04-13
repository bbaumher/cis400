package matchings;

import java.util.ArrayList;

public class Tree {
	
	protected ArrayList<TreeNode> nodeList;
	protected ArrayList<TreeEdge> edgeList;
	protected TreeNode root;

	public Tree(TreeNode node) {
		nodeList = new ArrayList<TreeNode>();
		edgeList = new ArrayList<TreeEdge>();
		nodeList.add(node);
		root = node;
	}
	
	public boolean containsNode(TreeNode node) {
		for (TreeNode n : nodeList) {
			if (n.equals(node)) return true;
		}
		return false;
	}
	
	public TreeNode getRoot() {
		return root;
	}
	

}
