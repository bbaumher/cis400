package matchings;

import java.util.ArrayList;

public class Tree {
	
	protected ArrayList<Node> nodeList;
	protected ArrayList<Edge> edgeList;

	public Tree(Node node) {
		nodeList = new ArrayList<Node>();
		edgeList = new ArrayList<Edge>();
		nodeList.add(node);
	}
	

}
