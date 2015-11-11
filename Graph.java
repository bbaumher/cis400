import java.util.Iterator;
import java.util.List;

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
		
	public Iterator<Node> getDFSIterator(Node s) {
		return new DFSIterator(s);
	}
	
	private class DFSIterator<Node> implements Iterator<Node> {
		
		public DFSIterator(Node s) {
			
		}

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Node next() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	

}
