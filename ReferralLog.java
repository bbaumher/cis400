import java.util.HashMap;
import java.util.Map;

public class ReferralLog {
	
	private final int numNeighbors;
	Map<Node,int[]> referralMap;
	
	public ReferralLog (int numNeighbors) {
		this.numNeighbors = numNeighbors;
		referralMap = new HashMap<Node,int[]>();
	}
	
	public int[] getReferral(Node node) {
		if (referralMap.containsKey(node)) {
			return referralMap.get(node);
		}
		else {
			int[] emptyReferral = new int[numNeighbors];
			referralMap.put(node, emptyReferral);
			return emptyReferral;
		}
	}
	
	public void setValue(Node node, int index, int value) {
		int[] values = getReferral(node);
		values[index] = value;
	}
	
	public void addValuesOnto(Node node1, Node node2) {
		int[] node1ref = getReferral(node1);
		int[] node2ref = getReferral(node2);
		for (int i = 0; i < numNeighbors; i++) {
			node2ref[i] = node1ref[i] + node2ref[i];
		}
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (Node v : referralMap.keySet()) {
			buf.append(v + " = ");
			for (int value : getReferral(v)) {
				buf.append(value + " ");
			}
			buf.append("\n");
		}
		return buf.toString();
	}

}
