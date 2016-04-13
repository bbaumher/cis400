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
	protected Map<Edge,TreeEdge> mapEdgeToTreeEdge;
	protected Map<TreeEdge,Edge> mapTreeEdgeToEdge;
	
	public Forest() {
		treeList = new ArrayList<Tree>();
		mapNodeToTreeNode = new TreeMap<Node,TreeNode>();
		mapTreeNodeToNode = new TreeMap<TreeNode,Node>();
		mapEdgeToTreeEdge = new TreeMap<Edge,TreeEdge>();
		mapTreeEdgeToEdge = new TreeMap<TreeEdge,Edge>();
	}
	
	public void addNewTree(Node node) {
		TreeNode treeNode = new TreeNode(node);
		mapNodeToTreeNode.put(node, treeNode);
		mapTreeNodeToNode.put(treeNode, node);
		Tree tree = new Tree(treeNode);
		treeList.add(tree);
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
		
		ArrayList<Edge> output = new ArrayList<Edge>();
		if (node1 == node2) return output; //zero length path
		
		TreeNode currentNode = treeNode1;
		while (true) {
			TreeNode nextNode = steps.get(currentNode);
			TreeEdge treeEdge = currentNode.getEdgeTo(nextNode);
			Edge edge = mapTreeEdgeToEdge.get(treeEdge);
			output.add(edge);
			if (nextNode == treeNode2) break;
			currentNode = nextNode;
		}
		return output;
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
			Edge edge = mapTreeEdgeToEdge.get(treeEdge);
			if (!edge.isMarked()) return edge;
		}
		return null;
	}
	
}
