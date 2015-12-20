
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ReadableGraph<T> {
  public void search() {
		//TODO
	}

	public abstract int getNodeCnt();
  
  public void printGraph() {
		System.out.println("Node size: " + getNodeCnt());

		for (
      Iterator<? extends ReadableNode<T>> iterator = getNodes().iterator();
      iterator.hasNext();
      )
    { // no self loops
      ReadableNode<T> n = iterator.next();
			System.out.print(n.getId() + ": ");
			boolean printedAlready = false;
			for (
        Iterator<? extends ReadableNode<T>> i = n.getAdjStream().iterator();
        i.hasNext();
        )
      {
        ReadableNode<T> j = i.next();
				if (printedAlready) {
					System.out.print(", ");
				}
				System.out.print(j.getId());
				printedAlready = true;
			}
			System.out.println();
		}

	}

	public abstract List<T> getNeighbors(T node);

  public Set<? extends ReadableNode<T>> getNeighbors(ReadableNode<T> n) {
		return n.getAdjSet();
	}

  public abstract List<T> getInboundNodes(T id);

  public abstract ReadableNode<T> getNode(T i);

  public abstract Stream<? extends ReadableNode<T>> getNodes();

	public Iterator<? extends ReadableNode<T>> getDFSIterator() {
    Optional<? extends ReadableNode<T>> node = getNodes().findAny();
		return
      node.isPresent()
        ? new DFSIterator(node.get())
        : Collections.emptyIterator();
	}

	public Iterator<? extends ReadableNode<T>> getDFSIterator(ReadableNode<T> s) {
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
	static <T> ReadableGraph<T> getSubGraph(
    ReadableGraph<T> graph,
    Set<T> nodes)
  {
		return new ReadableGraph<T>() {
			@Override
			public int getNodeCnt() {
				return nodes.size();
			}

			@Override
			public List<T> getNeighbors(T node) {
				return
					graph.getNeighbors(node)
						.stream()
						.filter(nodes::contains)
						.collect(Collectors.toList());
			}

			@Override
			public List<T> getInboundNodes(T id) {
				return
					graph.getInboundNodes(id)
						.stream()
						.filter(nodes::contains)
						.collect(Collectors.toList());
			}

			@Override
			public ReadableNode<T> getNode(T i) {
				return wrapNode(graph.getNode(i));
			}

			@Override
			public Stream<ReadableNode<T>> getNodes() {
				return
					graph.getNodes()
						.filter(node -> nodes.contains(node.getId()))
						.map(this::wrapNode);
			}

			private ReadableNode<T> wrapNode(ReadableNode<T> node) {
				return new ReadableNode<T>(node.getId()) {
          @Override
					Set<ReadableNode<T>> getAdjSet() {
						return getAdjStream().collect(Collectors.<ReadableNode<T>>toSet());
					}

					@Override
					Stream<? extends ReadableNode<T>> getAdjStream() {
						return
							node.getAdjStream()
								.filter(node -> nodes.contains(node.getId()));
					}
				};
			}
		};
	}

	private class DFSIterator implements Iterator<ReadableNode<T>> {

		private Set<ReadableNode<T>> seenNodes;
		private Stack<List<ReadableNode<T>>> adjListStack;
		private Stack<Integer> indexStack;
		private ReadableNode<T> nextNode;

		DFSIterator(ReadableNode<T> s) {
			seenNodes = new HashSet<>();
			adjListStack = new Stack<>();
			indexStack = new Stack<>();
			nextNode = s;

			seenNodes.add(s);
			adjListStack.add(
        s.getAdjStream().collect(Collectors.<ReadableNode<T>>toList()));
			indexStack.add(0);
		}

		@Override
		public boolean hasNext() {
			return nextNode != null;
		}

		@Override
		public ReadableNode<T> next() {
			ReadableNode<T> output = nextNode;
			nextNode = determineNextNode();
			return output;
		}

		private ReadableNode<T> determineNextNode() {
			if(adjListStack.isEmpty()) return null;

			List<ReadableNode<T>> adjList = adjListStack.peek();
			int index = indexStack.peek();
			if (index == adjList.size()) {
				adjListStack.pop();
				indexStack.pop();
				return determineNextNode();
			}

			ReadableNode<T> potentialNode = adjList.get(index);
			indexStack.push(indexStack.pop() + 1);//increment the last entry in the index stack

			if (seenNodes.contains(potentialNode)) {
				return determineNextNode();
			}

			seenNodes.add(potentialNode);
			adjListStack.add(
				potentialNode.getAdjStream()
          .collect(Collectors.<ReadableNode<T>>toList()));
			indexStack.add(0);
			return potentialNode;
		}

	}
}
