
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ReadableGraph {
  public void search() {
		//TODO
	}

	public abstract int getNodeCnt();
  
  public void printGraph() {
		System.out.println("Node size: " + getNodeCnt());

		for (
      Iterator<? extends ReadableNode> iterator = getNodes().iterator();
      iterator.hasNext();
      )
    { // no self loops
      ReadableNode n = iterator.next();
			System.out.print(n.getId() + ": ");
			boolean printedAlready = false;
			for (
        Iterator<? extends ReadableNode> i = n.getAdjStream().iterator();
        i.hasNext();
        )
      {
        ReadableNode j = i.next();
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

  public Set<? extends ReadableNode> getNeighbors(ReadableNode n) {
		return n.getAdjSet();
	}

  public abstract List<Integer> getInboundNodes(int id);

  public abstract ReadableNode getNode(int i);

  public abstract Stream<? extends ReadableNode> getNodes();

	public Iterator<? extends ReadableNode> getDFSIterator() {
		return new DFSIterator(getNode(0));
	}

	public Iterator<? extends ReadableNode> getDFSIterator(ReadableNode s) {
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
	static ReadableGraph getSubGraph(ReadableGraph graph, Set<Integer> nodes) {
		return new ReadableGraph() {
			@Override
			public int getNodeCnt() {
				return nodes.size();
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
			public ReadableNode getNode(int i) {
				return wrapNode(graph.getNode(i));
			}

			@Override
			public Stream<ReadableNode> getNodes() {
				return
					graph.getNodes()
						.filter(node -> nodes.contains(node.getId()))
						.map(this::wrapNode);
			}

			private ReadableNode wrapNode(ReadableNode node) {
				return new ReadableNode(node.getId()) {
          @Override
					Set<ReadableNode> getAdjSet() {
						return getAdjStream().collect(Collectors.<ReadableNode>toSet());
					}

					@Override
					Stream<? extends ReadableNode> getAdjStream() {
						return
							node.getAdjStream()
								.filter(node -> nodes.contains(node.getId()));
					}
				};
			}
		};
	}

	private class DFSIterator implements Iterator<ReadableNode> {

		private Set<ReadableNode> seenNodes;
		private Stack<List<ReadableNode>> adjListStack;
		private Stack<Integer> indexStack;
		private ReadableNode nextNode;

		DFSIterator(ReadableNode s) {
			seenNodes = new HashSet<>();
			adjListStack = new Stack<>();
			indexStack = new Stack<>();
			nextNode = s;

			seenNodes.add(s);
			adjListStack.add(
        s.getAdjStream().collect(Collectors.<ReadableNode>toList()));
			indexStack.add(0);
		}

		@Override
		public boolean hasNext() {
			return nextNode != null;
		}

		@Override
		public ReadableNode next() {
			ReadableNode output = nextNode;
			nextNode = determineNextNode();
			return output;
		}

		private ReadableNode determineNextNode() {
			if(adjListStack.isEmpty()) return null;

			List<ReadableNode> adjList = adjListStack.peek();
			int index = indexStack.peek();
			if (index == adjList.size()) {
				adjListStack.pop();
				indexStack.pop();
				return determineNextNode();
			}

			ReadableNode potentialNode = adjList.get(index);
			indexStack.push(indexStack.pop() + 1);//increment the last entry in the index stack

			if (seenNodes.contains(potentialNode)) {
				return determineNextNode();
			}

			seenNodes.add(potentialNode);
			adjListStack.add(
				potentialNode.getAdjStream()
          .collect(Collectors.<ReadableNode>toList()));
			indexStack.add(0);
			return potentialNode;
		}

	}
}
