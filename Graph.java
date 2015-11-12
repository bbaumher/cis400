import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public abstract class Graph {
	
	public abstract void search();
	
	public abstract int getNodeCnt();
	
	public abstract void addNode();
	
	public abstract void addEdge(Node i, Node j);

	public abstract void addEdge(int i, int j) ;
	
	public abstract void printGraph();

	public abstract List<Integer> getNeighbors(int node);
	
	public abstract List<Node> getNeighbors(Node n);
	
	public abstract List<Integer> getInboundNodes(int id); 
	
	public abstract Node getNode(int i);
	
	public Iterator<Node> getDFSIterator() {
		return new DFSIterator(getNode(0));
	}

	public Iterator<Node> getDFSIterator(Node s) {
		return new DFSIterator(s);
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
			adjListStack.add(s.getAdj());
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
			adjListStack.add(potentialNode.getAdj());
			indexStack.add(0);
			return potentialNode;
		}
		
	}
	
	
	

}
