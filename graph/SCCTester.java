package graph;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class SCCTester {

	public static <T> boolean isStronglyConnected(ReadableGraph<T> g) {
		
		Iterator<? extends ReadableNode<T>> iter1 = g.getDFSIterator();
		while (iter1.hasNext()) {
			ReadableNode<T> v = iter1.next();
			
			Iterator<? extends ReadableNode<T>> iter2 = g.getDFSIterator(v);
			int counter = 0;
			while (iter2.hasNext()) {
				iter2.next();
				counter++;
			}
			if (counter < g.getNodeCnt()) return false;
		}
		
		return true;
	}

	public static <T> Iterator<ReadableGraph<T>> getStronglyConnectedComponents(
    ReadableGraph<T> graph)
  {
		// Algorithm from
		// https://en.wikipedia.org/wiki/Path-based_strong_component_algorithm
		return new Iterator<ReadableGraph<T>>() {
			private final Iterator<? extends ReadableNode<T>> iterator =
        graph.getNodes().iterator();
			private ReadableNode<T> nextNode = null;
			private final Map<ReadableNode<T>, Integer> preorderNumbers =
				new HashMap<>(graph.getNodeCnt());
			private final Deque<ReadableNode<T>> unassignedStack = new ArrayDeque<>();
			private final Deque<ReadableNode<T>> nonloopingStack = new ArrayDeque<>();
			private final Deque<ReadableNode<T>> callStack = new ArrayDeque<>();
			private final Deque<Iterator<? extends ReadableNode<T>>> iterators =
        new ArrayDeque<>();
			
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
			public ReadableGraph<T> next() {
				while (true) {
					if (!hasNext()) {
						throw new NoSuchElementException();
					}

					if (callStack.isEmpty()) {
						pushOntoStacks(nextNode);
						nextNode = null;
					}

					while (iterators.peek().hasNext()) {
						ReadableNode<T> node = iterators.peek().next();
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

					ReadableNode<T> node = callStack.pop();
					iterators.pop();
					if (nonloopingStack.peek().equals(node)) {
						Set<T> result = new HashSet<>();
						nonloopingStack.pop();
						ReadableNode<T> nodeInComponent;
						do {
							nodeInComponent = unassignedStack.pop();
							result.add(nodeInComponent.getId());
							preorderNumbers.put(nodeInComponent, -1);
						} while (!node.equals(nodeInComponent));
						return ReadableGraph.getSubGraph(graph, result);
					}
				}
			}
			
			private void pushOntoStacks(ReadableNode<T> node) {
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
