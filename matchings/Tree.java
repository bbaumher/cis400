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
	
	public void addNewEdge(TreeNode oldTreeNode, TreeNode newTreeNode) {
		TreeEdge treeEdge = oldTreeNode.addEdgeTo(newTreeNode);
		nodeList.add(newTreeNode);
		edgeList.add(treeEdge);
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
	
	@Override
	public String toString() {
		String output = "{";
		for (int i = 0; i < nodeList.size(); i++) {
			if (i != 0) output += ",";
			output += nodeList.get(i);
		}
		output += "}";
		return output;
	}
	

}
