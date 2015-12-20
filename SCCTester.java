import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class SCCTester {

	public static boolean isStronglyConnected(Graph g) {
		
		Iterator<Node> iter1 = g.getDFSIterator();
		while (iter1.hasNext()) {
			Node v = iter1.next();
			
			Iterator<Node> iter2 = g.getDFSIterator(v);
			int counter = 0;
			while (iter2.hasNext()) {
				iter2.next();
				counter++;
			}
			if (counter < g.getNodeCnt()) return false;
		}
		
		return true;
	}

	public static Iterator<ReadableGraph> getStronglyConnectedComponents(
    Graph graph)
  {
		// Algorithm from
		// https://en.wikipedia.org/wiki/Path-based_strong_component_algorithm
		return new Iterator<ReadableGraph>() {
			private final Iterator<Node> iterator = graph.getNodes().iterator();
			private Node nextNode = null;
			private final Map<Node, Integer> preorderNumbers =
				new HashMap<>(graph.getNodeCnt());
			private final Deque<Node> unassignedStack = new ArrayDeque<>();
			private final Deque<Node> nonloopingStack = new ArrayDeque<>();
			private final Deque<Node> callStack = new ArrayDeque<>();
			private final Deque<Iterator<Node>> iterators = new ArrayDeque<>();
			
			@Override
			public boolean hasNext() {
				if (!callStack.isEmpty() || nextNode != null) {
					return true;
				}
				
				while (iterator.hasNext()) {
					nextNode = iterator.next();
					if (!preorderNumbers.containsKey(nextNode)) {
						return true;
					}
				}
				nextNode = null;
				return false;
			}

			@Override
			public ReadableGraph next() {
				while (true) {
					if (!hasNext()) {
						throw new NoSuchElementException();
					}

					if (callStack.isEmpty()) {
						pushOntoStacks(nextNode);
						nextNode = null;
					}

					while (iterators.peek().hasNext()) {
						Node node = iterators.peek().next();
						Integer preorderNumber = preorderNumbers.get(node);
						if (preorderNumber == null) {
							pushOntoStacks(node);
						}
						else if (preorderNumber >= 0) {
							while (
								preorderNumbers.get(nonloopingStack.peek())
									> preorderNumber)
							{
								nonloopingStack.pop();
							}
						}
					}

					Node node = callStack.pop();
					iterators.pop();
					if (nonloopingStack.peek().equals(node)) {
						Set<Integer> result = new HashSet<>();
						nonloopingStack.pop();
						Node nodeInComponent;
						do {
							nodeInComponent = unassignedStack.pop();
							result.add(nodeInComponent.getId());
							preorderNumbers.put(nodeInComponent, -1);
						} while (!node.equals(nodeInComponent));
						return ReadableGraph.getSubGraph(graph, result);
					}
				}
			}
			
			private void pushOntoStacks(Node node) {
				callStack.push(node);
				iterators.push(node.getAdjStream().iterator());
				unassignedStack.push(node);
				nonloopingStack.push(node);
				preorderNumbers.put(node, preorderNumbers.size());
			}
		};
	}

  private SCCTester() {
  }
}
