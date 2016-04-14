package matchings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Forest {
	
	protected ArrayList<Tree> treeList;
	protected Map<Node,TreeNode> mapNodeToTreeNode;
	protected Map<TreeNode,Node> mapTreeNodeToNode;
	
	public Forest() {
		treeList = new ArrayList<Tree>();
		mapNodeToTreeNode = new TreeMap<Node,TreeNode>();
		mapTreeNodeToNode = new TreeMap<TreeNode,Node>();
	}
	
	public void addNewTree(Node node) {
		TreeNode treeNode = new TreeNode(node);
		mapNodeToTreeNode.put(node, treeNode);
		mapTreeNodeToNode.put(treeNode, node);
		Tree tree = new Tree(treeNode);
		treeList.add(tree);
	}
	
	/** Assumes the old node is in a tree, the new node
	 *  is not in a tree, and there is no edge between them
	 *  at the moment.
	 */
	public void addNewEdge(Node oldNode, Node newNode) {
		TreeNode newTreeNode = new TreeNode(newNode);
		mapNodeToTreeNode.put(newNode, newTreeNode);
		mapTreeNodeToNode.put(newTreeNode, newNode);
		TreeNode oldTreeNode = mapNodeToTreeNode.get(oldNode);
		Tree tree = getTreeWithNode(oldNode);
		tree.addNewEdge(oldTreeNode, newTreeNode);
	}
	
	public boolean containsNode(Node node) {
		return mapNodeToTreeNode.get(node) != null;
	}
	
	public Tree getTreeWithNode(Node node) {
		TreeNode treeNode = mapNodeToTreeNode.get(node);
		for (Tree tree : treeList) {
			if (tree.containsNode(treeNode)) return tree;
		}
		return null;
	}
	
	public Node getRootOfNode(Node node) {
		Tree tree = getTreeWithNode(node);
		TreeNode treeNodeRoot = tree.getRoot();
		Node root = mapTreeNodeToNode.get(treeNodeRoot);
		return root;
	}
	
	public int getDistanceToRoot(Node node) {
		Node root = getRootOfNode(node);
		int dist = getDistance(node, root);
		return dist;
	}
	
	public ArrayList<Edge> getPathFromNodeToRoot(Node node) {
		Node root = getRootOfNode(node);
		ArrayList<Edge> path = getPath(node, root);
		return path;
	}
	
	public ArrayList<Edge> getPathFromRootToNode(Node node) {
		Node root = getRootOfNode(node);
		ArrayList<Edge> path = getPath(root, node);
		return path;
	}
	
	public int getDistance(Node node1, Node node2) {
		TreeNode treeNode1 = mapNodeToTreeNode.get(node1);
		TreeNode treeNode2 = mapNodeToTreeNode.get(node2);
		
		Map<TreeNode,TreeNode> steps = new TreeMap<TreeNode,TreeNode>();
		Map<TreeNode,Integer> distances = new TreeMap<TreeNode,Integer>();
		Set<TreeNode> coveredNodes = new TreeSet<TreeNode>();
		Queue<TreeNode> pendingNodes = new LinkedList<TreeNode>();
		
		pendingNodes.add(treeNode1);
		coveredNodes.add(treeNode1);
		distances.put(treeNode1, 0);

		while(!pendingNodes.isEmpty()) {
			TreeNode dequeue = pendingNodes.poll();
			for (TreeNode neighbor : dequeue.getNeighbors()) {
				if (coveredNodes.contains(neighbor)) continue;
				steps.put(dequeue, neighbor);
				distances.put(neighbor, distances.get(dequeue)+1);
				coveredNodes.add(neighbor);
				pendingNodes.add(neighbor);
			}
		}
		
		return distances.get(treeNode2);
	}
	
	public ArrayList<Edge> getPath(Node node1, Node node2) {
		TreeNode treeNode1 = mapNodeToTreeNode.get(node1);
		TreeNode treeNode2 = mapNodeToTreeNode.get(node2);
		
		Map<TreeNode,TreeNode> steps = new TreeMap<TreeNode,TreeNode>();
		Map<TreeNode,Integer> distances = new TreeMap<TreeNode,Integer>();
		Set<TreeNode> coveredNodes = new TreeSet<TreeNode>();
		Queue<TreeNode> pendingNodes = new LinkedList<TreeNode>();
		
		pendingNodes.add(treeNode1);
		coveredNodes.add(treeNode1);
		distances.put(treeNode1, 0);

		while(!pendingNodes.isEmpty()) {
			TreeNode dequeue = pendingNodes.poll();
			for (TreeNode neighbor : dequeue.getNeighbors()) {
				if (coveredNodes.contains(neighbor)) continue;
				steps.put(dequeue, neighbor);
				distances.put(neighbor, distances.get(dequeue)+1);
				coveredNodes.add(neighbor);
				pendingNodes.add(neighbor);
			}
		}
		
		ArrayList<Edge> path = new ArrayList<Edge>();
		if (node1 == node2) return path; //zero length path
		
		TreeNode currentNode = treeNode1;
		while (true) {
			TreeNode nextNode = steps.get(currentNode);
			TreeEdge treeEdge = currentNode.getEdgeTo(nextNode);
			Edge edge = mapTreeEdgeToEdge(treeEdge);
			path.add(edge);
			if (nextNode == treeNode2) break;
			currentNode = nextNode;
		}
		return path;
	}
	
	public Node getUnmarkedVertexWithEvenDistanceToRoot() {
		for (Tree tree : treeList) {
			for (TreeNode treeNode : tree.nodeList) {
				Node node = mapTreeNodeToNode.get(treeNode);
				if (!node.isMarked() && getDistanceToRoot(node) % 2 == 0) {
					return node;
				}
			}
		}
		return null;
	}
	
	public Edge getUnmarkedEdgeIncidentToNode(Node node) {
		TreeNode treeNode = mapNodeToTreeNode.get(node);
		if (treeNode == null) return null; //don't have this node in the tree
		for (TreeEdge treeEdge : treeNode.edgeList) {
			Edge edge = mapTreeEdgeToEdge(treeEdge);
			if (!edge.isMarked()) return edge;
		}
		return null;
	}
	
	public Edge mapTreeEdgeToEdge(TreeEdge treeEdge) {
		TreeNode treeNode1 = treeEdge.getNode1();
		TreeNode treeNode2 = treeEdge.getNode2();
		Node node1 = mapTreeNodeToNode.get(treeNode1);
		Node node2 = mapTreeNodeToNode.get(treeNode2);
		return node1.getEdgeTo(node2);
	}
	
	public TreeEdge mapEdgeToTreeEdge(Edge edge) {
		Node node1 = edge.getNode1();
		Node node2 = edge.getNode2();
		TreeNode treeNode1 = mapNodeToTreeNode.get(node1);
		TreeNode treeNode2 = mapNodeToTreeNode.get(node2);
		return treeNode1.getEdgeTo(treeNode2);
	}
	
	@Override
	public String toString() {
		String output = "{";
		for (int i = 0; i < treeList.size(); i++) {
			if (i != 0) output += ", ";
			output += treeList.get(i);
		}
		output += "}";
		return output;
	}
	
}
