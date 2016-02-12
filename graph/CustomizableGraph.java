package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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
		throw new UnsupportedOperationException("Can't call add node with no input.");
	}
	
	@Override
	public void addEdge(Node<T> ni, Node<T> nj) {
		ni.addEdge(nj);
	}
	
	public void addUndirectedEdge(Node<T> ni, Node<T> nj) {
		ni.addEdge(nj);
		nj.addEdge(ni);
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
		addEdge(ni,nj);
	}
	
	public void addUndirectedEdge(T i, T j) {
		Node<T> ni = map.get(i);
		Node<T> nj = map.get(j);
		addUndirectedEdge(ni,nj);
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
		return
      getNodes()
        .filter(
          node ->
            node.getAdjStream().anyMatch(n -> Objects.equals(n.getId(), id)))
        .map(ReadableNode::getId)
        .collect(Collectors.<T>toList());
	}

}
