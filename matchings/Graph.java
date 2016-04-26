package matchings;
import java.util.ArrayList;

public class Graph {
	
	protected final ArrayList<Node> nodeList;
	protected final ArrayList<Edge> edgeList;
	
	public Graph (int numNodes) {
		nodeList = new ArrayList<Node>();
		for(int i = 0; i < numNodes; i++) {
			nodeList.add(i, new Node("n" + i));
		}
		edgeList = new ArrayList<Edge>();
	}
	
	public Graph (int numNodes, String string) {
		nodeList = new ArrayList<Node>();
		for(int i = 0; i < numNodes; i++) {
			nodeList.add(i, new Node(string + i));
		}
		edgeList = new ArrayList<Edge>();
	}
	
	public Graph (int numNodes, double prob) {
		nodeList = new ArrayList<Node>();
		for(int i = 0; i < numNodes; i++) {
			nodeList.add(i, new Node("n" + i));
		}
		edgeList = new ArrayList<Edge>();
		
		for (int i = 0; i < nodeList.size(); i++) {
			for (int j = i+1; j < nodeList.size(); j++) {
				if (Math.random() < prob) {
					addEdgeBetweenNodes(nodeList.get(i),nodeList.get(j));
				}
			}
		}
	}
	
	public Graph (int numNodes, char c) {
		this(numNodes);
		
		if (c == 'a') { //numNodes = 8
			Node n0 = nodeList.get(0);
			Node n1 = nodeList.get(1);
			Node n2 = nodeList.get(2);
			Node n3 = nodeList.get(3);
			Node n4 = nodeList.get(4);
			Node n5 = nodeList.get(5);
			Node n6 = nodeList.get(6);
			Node n7 = nodeList.get(7);
			
			addEdgeBetweenNodes(n0,n1);
			addEdgeBetweenNodes(n0,n4);
			addEdgeBetweenNodes(n0,n5);
			addEdgeBetweenNodes(n1,n2);
			addEdgeBetweenNodes(n1,n4);
			addEdgeBetweenNodes(n1,n5);
			addEdgeBetweenNodes(n1,n6);
			addEdgeBetweenNodes(n2,n3);
			addEdgeBetweenNodes(n2,n5);
			addEdgeBetweenNodes(n2,n6);
			addEdgeBetweenNodes(n2,n7);
			addEdgeBetweenNodes(n3,n6);
			addEdgeBetweenNodes(n3,n7);
			addEdgeBetweenNodes(n4,n5);
			addEdgeBetweenNodes(n5,n6);
			addEdgeBetweenNodes(n6,n7);
		}
		
		if (c == 'b') { //numNodes = 4
			Node n0 = nodeList.get(0);
			Node n1 = nodeList.get(1);
			Node n2 = nodeList.get(2);
			Node n3 = nodeList.get(3);
			
			addEdgeBetweenNodes(n0,n1);
			addEdgeBetweenNodes(n0,n2);
			addEdgeBetweenNodes(n0,n3);
			addEdgeBetweenNodes(n1,n2);
			addEdgeBetweenNodes(n1,n3);
			addEdgeBetweenNodes(n2,n3);
		}
		
		else {
			
		}
	}
	
	//won't add an edge if there's already one there
	public void addEdgeBetweenNodes(Node n1, Node n2) {
		if(n1.hasEdgeTo(n2)) return;
		Edge e = n1.addEdgeTo(n2);
		this.edgeList.add(e);
	}
	
	//won't add an edge if there's already one there
	public void addEdgeBetweenNodes(int i1, int i2) {
		addEdgeBetweenNodes(getNodeIndexedAt(i1),getNodeIndexedAt(i2));
	}
	
	public void addNode(Node node) {
		nodeList.add(node);
	}
	
	public Edge getEdgeIndexedAt(int index) {
		return edgeList.get(index);
	}
	
	public Node getNodeIndexedAt(int index) {
		return nodeList.get(index);
	}
	
	public int getIndexOfEdge(Edge edge) {
		for (int i = 0; i < edgeList.size(); i++) {
			if (edge == edgeList.get(i)) return i;
		}
		return -1;
	}
	
	public int getIndexOfNode(Node node) {
		for (int i = 0; i < nodeList.size(); i++) {
			if (node == nodeList.get(i)) return i;
		}
		return -1;
	}
	
	public int getEdgeCount() {
		return edgeList.size();
	}
	
	public int getNodeCount() {
		return nodeList.size();
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("Nodes: ");
		boolean nodeAdded = false;
		for (Node node : nodeList) {
			if (nodeAdded) buf.append(", ");
			buf.append(node);
			nodeAdded = true;
		}
		
		buf.append("\nEdges: ");
		boolean edgeAdded = false;
		for (Edge edge : edgeList) {
			if (edgeAdded) buf.append(", ");
			buf.append(edge);
			edgeAdded = true;
		}
		
		return buf.toString();
	}
	
	public ArrayList<Node> getNodeList() {
		return nodeList;
	}
	
	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}

	public Edge getUnmarkedEdgeIncidentToNode(Node node) {
		for (Edge edge : node.getEdgeList()) {
			if (!edge.isMarked()) return edge;
		}
		return null;
	}

}
