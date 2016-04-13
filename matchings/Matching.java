package matchings;
import java.util.ArrayList;
import java.util.Arrays;

public class Matching implements Comparable<Matching> {
	
	protected final Graph myGraph;
	protected final boolean[] edgeBooleans;
	protected final boolean[] nodeBooleans;
	
	public Matching(Graph g) {
		myGraph = g;
		edgeBooleans = new boolean[myGraph.getEdgeCount()];
		nodeBooleans = new boolean[myGraph.getNodeCount()];
	}
	
	public void fill() {
		for (int nodeIndex = 0; nodeIndex < nodeBooleans.length; nodeIndex++) {
			boolean nodeBool = nodeBooleans[nodeIndex];
			if (nodeBool == true) continue; //node already covered
			Node node = myGraph.getNodeIndexedAt(nodeIndex);
			for (Node neighbor : node.getNeighbors()) {
				int neighborIndex = myGraph.getIndexOfNode(neighbor);
				boolean neighborBool = nodeBooleans[neighborIndex];
				if (neighborBool == true) continue; //can't add this edge since that node is used already
				Edge edge = node.getEdgeTo(neighbor);
				int edgeIndex = myGraph.getIndexOfEdge(edge);
				
				//neither node is covered yet - add the edge between them
				nodeBooleans[nodeIndex] = true;
				nodeBooleans[neighborIndex] = true;
				edgeBooleans[edgeIndex] = true;
				break;
			}
		}
	}
	
	public void fillSmartly() {
		BlossomAlgorithm.runAlgorithm(this);
	}
	
	public ArrayList<Matching> getNeighborMatchings() {
		ArrayList<Matching> output = new ArrayList<Matching>();
		
		if (isPerfect()) { //is perfect
			for (int i = 0; i < edgeBooleans.length; i++) {
				if (edgeBooleans[i]) { //edge is present
					Edge edge = myGraph.getEdgeIndexedAt(i);
					output.add(clone().removeEdge(edge));
				}
			}
		}
		else { //is not perfect
			for (int i = 0; i < nodeBooleans.length; i++) {
				if (!nodeBooleans[i]) { //node is absent
					Node node = myGraph.getNodeIndexedAt(i);
					for (Edge edge : node.getEdgeList()) {
						output.add(clone().addEdge(edge));
					}
				}
			}
		}
		
		return output;
	}

  public boolean isPerfect() {
		for (boolean bool : nodeBooleans) {
			if (!bool) {
        return false;
      }
		}
    
    return true;
  }

  public int matchedVertexCount() {
    int result = 0;
    for (boolean bool : nodeBooleans) {
			if (bool) {
        result++;
      }
		}

    return result;
  }
	
	//removes the specified edge removed
	//also, the nodes belonging to that edge are removed
	protected Matching removeEdge(Edge edge) {
		int edgeIndex = myGraph.getIndexOfEdge(edge);
		int node1Index = myGraph.getIndexOfNode(edge.getNode1());
		int node2Index = myGraph.getIndexOfNode(edge.getNode2());
		
		edgeBooleans[edgeIndex] = false;
		nodeBooleans[node1Index] = false;
		nodeBooleans[node2Index] = false;
		
		return this;
	}
	
	//removes the specified node
	//also, the edge (if any) belonging to that node is removed
	protected Matching removeNode(Node node) {
		Edge edge = getEdgeIncidentToNode(node);
		if (edge != null) removeEdge(edge);
		
		return this;
	}
	
	//adds the specified edge to this matching
	//also, the nodes belonging to that edge are disconnected from any other edges
	protected Matching addEdge(Edge edge) {
		int edgeIndex = myGraph.getIndexOfEdge(edge);
		Node node1 = edge.getNode1();
		int node1Index = myGraph.getIndexOfNode(node1);
		Node node2 = edge.getNode2();
		int node2Index = myGraph.getIndexOfNode(node2);
		
		if (hasNode(node1)) removeNode(node1);
		if (hasNode(node2)) removeNode(node2);
		edgeBooleans[edgeIndex] = true;
		nodeBooleans[node1Index] = true;
		nodeBooleans[node2Index] = true;
		
		return this;
	}
	
	//gets the edge of this matching incident to the specified node
	protected Edge getEdgeIncidentToNode(Node node) {
		for(Edge edge : node.getEdgeList()) {
			if (hasEdge(edge)) return edge;
		}
		return null;
	}
	
	//creates a clone of this
	public Matching clone() {
		Matching clone = new Matching(myGraph);
		for (int i = 0; i < edgeBooleans.length; i++) {
			clone.edgeBooleans[i] = this.edgeBooleans[i];
		}
		for (int i = 0; i < nodeBooleans.length; i++) {
			clone.nodeBooleans[i] = this.nodeBooleans[i];
		}
		return clone;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("edgeBooleans: [");
		boolean edgeBoolAdded = false;
		for (boolean bool : edgeBooleans) {
			if (edgeBoolAdded) buf.append(" ");
			buf.append(bool ? "1" : "0");
			edgeBoolAdded = true;
		}
		buf.append("]");
		
		buf.append("\nedges: ");
		boolean edgeAdded = false;
		for (int i = 0; i < edgeBooleans.length; i++) {
			boolean bool = edgeBooleans[i];
			if (bool) {
				if (edgeAdded) buf.append(", ");
				buf.append(myGraph.getEdgeIndexedAt(i));
				edgeAdded = true;
			}
		}
		
		buf.append("\nnodeBooleans: [");
		boolean nodeBoolAdded = false;
		for (boolean bool : nodeBooleans) {
			if (nodeBoolAdded) buf.append(" ");
			buf.append(bool ? "1" : "0");
			nodeBoolAdded = true;
		}
		buf.append("]");
		
		buf.append("\nnodes: ");
		boolean nodeAdded = false;
		for (int i = 0; i < nodeBooleans.length; i++) {
			boolean bool = nodeBooleans[i];
			if (bool) {
				if (nodeAdded) buf.append(", ");
				buf.append(myGraph.getNodeIndexedAt(i));
				nodeAdded = true;
			}
		}
		
		return buf.toString();
	}
	
	public String toString2() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("{");
		boolean edgeAdded = false;
		for (int i = 0; i < edgeBooleans.length; i++) {
			boolean bool = edgeBooleans[i];
			if (bool) {
				if (edgeAdded) buf.append(",");
				buf.append(myGraph.getEdgeIndexedAt(i));
				edgeAdded = true;
			}
		}
		buf.append("}");
		
		return buf.toString();
	}
	
	protected boolean hasEdge(Edge edge) {
		int index = myGraph.getIndexOfEdge(edge);
		boolean bool = edgeBooleans[index];
		return bool;
	}
	
	protected boolean hasEdge(int index) {
		boolean bool = edgeBooleans[index];
		return bool;
	}
	
	protected boolean hasNode(Node node) {
		int index = myGraph.getIndexOfNode(node);
		boolean bool = nodeBooleans[index];
		return bool;
	}
	
	protected boolean hasNode(int index) {
		boolean bool = nodeBooleans[index];
		return bool;
	}
	
	/** Returns whether the inputted node is exposed,
	 *  i.e. there are node edges incident to it.
	 */
	protected boolean isExposed(Node node) {
		for (Edge edge : node.getEdgeList()) {
			if (this.hasEdge(edge)) return false;
		}
		return true;
	}
	
	protected Edge getIncidentEdge(Node node) {
		for (Edge edge : node.getEdgeList()) {
			if (this.hasEdge(edge)) return edge;
		}
		return null;
	}
	
	protected Node getIncidentNode(Node node) {
		for (Edge edge : node.getEdgeList()) {
			if (this.hasEdge(edge)) return edge.getOther(node);
		}
		return null;
	}

	@Override
	public int compareTo(Matching other) {
		int diff;
		
		diff = this.edgeBooleans.length - other.edgeBooleans.length;
		if (diff != 0) return diff;
		
		for (int i = 0; i < edgeBooleans.length; i++) {
			diff = (this.edgeBooleans[i] ? 1 : 0) - (other.edgeBooleans[i] ? 1 : 0);
			if (diff != 0) return diff;
		}
		
		diff = this.nodeBooleans.length - other.nodeBooleans.length;
		if (diff != 0) return diff;
		
		for (int i = 0; i < nodeBooleans.length; i++) {
			diff = (this.nodeBooleans[i] ? 1 : 0) - (other.nodeBooleans[i] ? 1 : 0);
			if (diff != 0) return diff;
		}
		
		return 0;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Matching)) return false;
		Matching m = (Matching) other;
		
		if (this.compareTo(m) == 0) return true;
		else return false;
	}
	
	/** Two Matchings are equal if their edgeBooleans array and
	 *  nodeBooleans array have the same size and values.
	 */
	@Override
	public int hashCode() {
		int hash = 37;
		for (int i = 0; i < edgeBooleans.length; i++) {
			int d = edgeBooleans[i] ? 3 : 7;
			hash += d * (i+1);
			hash *= d;
		}
		for (int i = 0; i < nodeBooleans.length; i++) {
			int d = nodeBooleans[i] ? 11 : 13;
			hash += d * (i+3);
			hash *= d;
		}
		return hash;
	}

}
