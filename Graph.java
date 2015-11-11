import java.util.List;


public interface Graph {
	
	public void search();
	
	public int getNodeCnt();
	
	public void addNode();
	
	public void addEdge(Node i, Node j);

	public void addEdge(int i, int j) ;
	
	public List<Integer> getNeighbors(int node);
	
	public List<Node> getNeighbors(Node n);
	
	public List<Integer> getInboundNodes(int id); 
	
	public void printGraph();
	

	
	
	

}
