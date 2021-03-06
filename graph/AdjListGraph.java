package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AdjListGraph extends Graph<Integer> { // undirected graph
	int nodeCnt;
	ArrayList<Node<Integer>> nodeList;
	
	public AdjListGraph(int n) {
		nodeCnt = n;
		nodeList = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			nodeList.add(new AdjListNode(i));
		}
	}

  AdjListGraph(ReadableGraph<?> graph) {
    this(graph.getNodeCnt());
    Map<ReadableNode<?>, Integer> nodes = new HashMap<>();
    graph.getNodes()
      .forEach(
        node -> {
          if (!nodes.containsKey(node)) {
            nodes.put(node, nodes.size());
          }
        }
      );
    graph.getNodes()
      .forEach(
        node ->
          node.getAdjStream()
            .forEach(neighbor -> addEdge(nodes.get(node), nodes.get(neighbor)))
      );
  }

	@Override
	public int getNodeCnt() {
		return nodeCnt;
	}
	
	@Override
	public Node<Integer> addNode() {
		Node<Integer> node = new AdjListNode(nodeCnt);
		nodeCnt++;
		nodeList.add(node);
		return node;
	}
	
	@Override
	public void addEdge(Integer i, Integer j) throws IllegalArgumentException {
		if (i >= nodeCnt || j >= nodeCnt) {
			throw new IllegalArgumentException("bad index");
		}
		nodeList.get(i).addEdge(nodeList.get(j));	
	}
	
	 
	@Override
	public void addEdge(Node<Integer> i, Node<Integer> j) {
		i.addEdge(j);
	}
	
	@Override
	public Node<Integer> getNode(Integer index) {
		return index < 0 || index >= nodeCnt ? null : nodeList.get(index);
	}

	@Override
	public List<Integer> getNeighbors(Integer id) {
		Node<Integer> node = this.getNode(id);
		Iterable<Node<Integer>> neighbors = node.getAdjStream()::iterator;
		List<Integer> neighborIds = new ArrayList<>();
		for ( Node<Integer> n : neighbors ) {
			neighborIds.add(n.getId());	
		}
		return neighborIds;
	}

	@Override
	public List<Integer> getInboundNodes(Integer id) {
		List<Integer> neighbors = new ArrayList<>();
		Node<Integer> node = this.getNode(id);
		for (Node<Integer> n : nodeList) {
			if (n.getAdjSet().contains(node)) {
				neighbors.add(n.getId());
			}
		}
		return neighbors;
	}
	
	public List<Node<Integer>> getInboundNodes(Node<Integer> node) {
		List<Node<Integer>> neighbors = new ArrayList<>();
		
		for (Node<Integer> n : nodeList) {
			if (n.getAdjSet().contains(node)) {
				neighbors.add(n);
			}
		}
		return neighbors;
	}

	@Override
	public Stream<Node<Integer>> getNodes() {
		return IntStream.range(0, getNodeCnt()).mapToObj(this::getNode);
	}
	
	private static final class AdjListNode extends Node<Integer> {
		private Set<Node<Integer>> adjSet;

		AdjListNode (int id) {
			super(id);
			this.adjSet = new HashSet<>();
		}

		@Override
		public void addEdge(Node<Integer> a) {
			adjSet.add(a);
		}

		@Override
		public Set<Node<Integer>> getAdjSet() {
			return adjSet;
		}

		@Override
		public Stream<Node<Integer>> getAdjStream() {
			return getAdjSet().stream();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		for (Node<Integer> v : nodeList) {
			output.append(v).append(": ");
			boolean printedAlready = false;
			for (Node<Integer> u : v.getAdjSet()) {
				if (printedAlready) {
					output.append(", ");
				}
				output.append(u);
				printedAlready = true;
			}
			output.append("\n");
		}
		return output.toString();
	}
}
