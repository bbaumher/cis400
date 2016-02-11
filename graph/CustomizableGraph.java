package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CustomizableGraph<T> extends Graph<T> {
	
	ArrayList<Node<T>> nodes;
	Map<T,Node<T>> map;
	
	public CustomizableGraph () {
		super();
		nodes = new ArrayList<Node<T>>();
		map = new HashMap<T,Node<T>>();
	}

	@Override @Deprecated
	public Node<T> addNode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override @Deprecated
	public void addEdge(Node<T> i, Node<T> j) {
		// TODO Auto-generated method stub
		
	}
	
	public CustomizableNode<T> addNode(T t) {
		CustomizableNode<T> node = new CustomizableNode<T>(t);
		nodes.add(node);
		map.put(t, node);
		return node;
	}

	@Override
	public void addEdge(T i, T j) {
		Node<T> ni = map.get(i);
		Node<T> nj = map.get(j);
		ni.addEdge(nj);
	}

	@Override
	public Node<T> getNode(T i) {
		return map.get(i);
	}

	@Override
	public Stream<Node<T>> getNodes() {
		return nodes.stream();
	}

	@Override
	public int getNodeCnt() {
		return nodes.size();
	}

	@Override
	public List<T> getNeighbors(T t) {
		Node<T> node = map.get(t);
		List<T> neighbors = new ArrayList<T>();
		for (Node<T> n : node.getAdjSet()) {
			neighbors.add(n.getId());
		}
		return neighbors;
	}

	@Override
	public List<T> getInboundNodes(T id) {
		List<T> ts = new ArrayList<T>();
		for (Node<T> n : nodes) {
			ts.add(n.getId());
		}
		return ts;
	}

}
