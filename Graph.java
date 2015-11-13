import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Graph {
	
	public void search() {
		//TODO
	}
	
	public abstract int getNodeCnt();
	
	public abstract Node addNode();
	
	public abstract void addEdge(Node i, Node j);

	public abstract void addEdge(int i, int j) ;
	
	public void printGraph() {
		System.out.println("Node size: " + getNodeCnt());
		
		for (Node n : (Iterable<Node>) getNodes()::iterator) { // no self loops
			System.out.print(n.getId() + ": ");
			boolean printedAlready = false;
			for (Node j : (Iterable<Node>) n.getAdjStream()::iterator) {
				if (printedAlready) {
					System.out.print(", ");
				}
				System.out.print(j.getId());
				printedAlready = true;
			}
			System.out.println();
		}
		
	}

	public abstract List<Integer> getNeighbors(int node);
	
	public Set<Node> getNeighbors(Node n) {
		return n.getAdjSet();
	}
	
	public abstract List<Integer> getInboundNodes(int id); 
	
	public abstract Node getNode(int i);
	
	public abstract Stream<Node> getNodes();
	
	public Iterator<Node> getDFSIterator() {
		return new DFSIterator(getNode(0));
	}

	public Iterator<Node> getDFSIterator(Node s) {
		return new DFSIterator(s);
	}
	
	/**
	 * Return a {@link Graph} that contains all of the vertices of {@code graph}
	 * whose ids are contained in {@code nodes}, as well as all the edges from
	 * {@code graph} between two such nodes. The new {@link Graph} is backed by
	 * the passed-in {@link Graph}, and any modifications to either will be seen
	 * by the other.
	 * 
	 * @param graph The {@link Graph} for which we want to produce a subgraph.
	 * @param nodes The {@link Set} of ids for the nodes that the subgraph
	 * should contain.
	 * @return A {@link Graph} over the indicated subset of nodes.
	 */
	static Graph getSubGraph(Graph graph, Set<Integer> nodes) {
		return new Graph() {
			@Override
			public int getNodeCnt() {
				return nodes.size();
			}

			@Override
			public Node addNode() {
				Node node = graph.addNode();
				if (node != null) {
					nodes.add(node.getId());
				}
				return wrapNode(node);
			}

			@Override
			public void addEdge(Node i, Node j) {
				graph.addEdge(i, j);
			}

			@Override
			public void addEdge(int i, int j) {
				graph.addEdge(i, j);
			}

			@Override
			public List<Integer> getNeighbors(int node) {
				return
					graph.getNeighbors(node)
						.stream()
						.filter(nodes::contains)
						.collect(Collectors.toList());
			}

			@Override
			public List<Integer> getInboundNodes(int id) {
				return
					graph.getInboundNodes(id)
						.stream()
						.filter(nodes::contains)
						.collect(Collectors.toList());
			}

			@Override
			public Node getNode(int i) {
				return wrapNode(graph.getNode(i));
			}

			@Override
			public Stream<Node> getNodes() {
				return
					graph.getNodes()
						.filter(node -> nodes.contains(node.getId()))
						.map(this::wrapNode);
			}
			
			private Node wrapNode(Node node) {
				return new Node(node.getId()) {
					@Override
					void addEdge(Node a) {
						node.addEdge(a);
					}

					@Override
					Set<Node> getAdjSet() {
						return getAdjStream().collect(Collectors.toSet());
					}

					@Override
					Stream<Node> getAdjStream() {
						return
							node.getAdjStream()
								.filter(node -> nodes.contains(node.getId()));
					}
				};
			}
		};
	}
	
	private class DFSIterator implements Iterator<Node> {
		
		private Set<Node> seenNodes;
		private Stack<List<Node>> adjListStack;
		private Stack<Integer> indexStack;
		private Node nextNode;
		
		public DFSIterator(Node s) {
			seenNodes = new HashSet<Node>();
			adjListStack = new Stack<List<Node>>();
			indexStack = new Stack<Integer>();
			nextNode = s;
			
			seenNodes.add(s);
			adjListStack.add(s.getAdjStream().collect(Collectors.toList()));
			indexStack.add(0);
		}

		@Override
		public boolean hasNext() {
			return nextNode != null;
		}

		@Override
		public Node next() {
			Node output = nextNode;
			nextNode = determineNextNode();
			return output;
		}
		
		private Node determineNextNode() {
			if(adjListStack.isEmpty()) return null;
			
			List<Node> adjList = adjListStack.peek();
			int index = indexStack.peek();
			if (index == adjList.size()) {
				adjListStack.pop();
				indexStack.pop();
				return determineNextNode();
			}
			
			Node potentialNode = adjList.get(index);
			indexStack.push(indexStack.pop() + 1);//increment the last entry in the index stack
			
			if (seenNodes.contains(potentialNode)) {
				return determineNextNode();
			}
			
			seenNodes.add(potentialNode);
			adjListStack.add(
				potentialNode.getAdjStream().collect(Collectors.toList()));
			indexStack.add(0);
			return potentialNode;
		}
		
	}
	
	
	

}
