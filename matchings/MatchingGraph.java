package matchings;

import graph.AdjListGraph;

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
	
	public AdjListGraph toAdjListGraph() {
		AdjListGraph output = new AdjListGraph(this.getNodeCount());
		
		for (int i = 0; i < this.getNodeCount(); i++) {
			for (int j = i+1; j < this.getNodeCount(); j++) {
				Node n1 = this.getNodeIndexedAt(i);
				Node n2 = this.getNodeIndexedAt(j);
				if (n1.hasEdgeTo(n2)) output.addEdge(i, j);
			}
		}
		
		return output;
	}

}
