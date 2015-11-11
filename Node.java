import java.util.ArrayList;
import java.util.List;

public class Node {
	
	int id;
	private List<Node> adjList;
	
	public Node (String info) {
		
	}
	
	public Node (int id) {
		this.id = id;
		this.adjList = new ArrayList<Node>();
	}

	
	public Node() {
		
	}
	
	public int getId() {
		return id;
	}
	
	public void addEdge(Node a) {
		adjList.add(a);
	}
	
	public List<Node> getAdj() {
		return adjList;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public String toString() {
		return id + "";
	}
	

}
