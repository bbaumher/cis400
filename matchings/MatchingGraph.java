package matchings;


public class MatchingGraph extends Graph {

	public MatchingGraph() {
		super(0);	
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("Nodes:\n");
		boolean nodeAdded = false;
		for (Node node : nodeList) {
			if (nodeAdded) buf.append("\n");
			buf.append(node);
			nodeAdded = true;
		}
		
		buf.append("\nEdges:\n");
		boolean edgeAdded = false;
		for (Edge edge : edgeList) {
			if (edgeAdded) buf.append("\n");
			buf.append(edge);
			edgeAdded = true;
		}
		
		return buf.toString();
	}

}
