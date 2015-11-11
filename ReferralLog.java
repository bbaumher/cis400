import java.util.HashMap;
import java.util.Map;

/**
 * @author Scott
 *
 * The ReferralLog class maintains a map from Node to int[].
 * Each entry of the int[] corresponds to one of s's neighbors.
 * The value of the entry represents how many shortest paths there are
 * from s to that node which pass through the neighbor of s represented
 * by that entry.
 * 
 * For example, if node v has array [0 2 1], then there are:
 * - 0 shortest path(s) from s to v that pass through neighbor 0
 * - 2 shortest path(s) from s to v that pass through neighbor 2
 * - 1 shortest path(s) from s to v that pass through neighbor 1
 */

public class ReferralLog {
	
	private final int numNeighbors; //the number of neighbors s has
	Map<Node,int[]> referralMap; //the actual mapping from Node to int[]
	
	/** Construct a ReferralLog by storing the number of neighbors and
	 *  instantiating the map.
	 * 
	 * @param numNeighbors the number of neighbors s has
	 */
	public ReferralLog (int numNeighbors) {
		this.numNeighbors = numNeighbors;
		referralMap = new HashMap<Node,int[]>();
	}
	
	/** Get the referral of a node, i.e. its mapped to int[].
	 *  If it doesn't have one yet, then an empty one is created,
	 *  added to the mapping, and returned.
	 */
	public int[] getReferral(Node node) {
		if (referralMap.containsKey(node)) { //node already has a referral array
			return referralMap.get(node);
		}
		else { //node doesn't have a referral array yet
			int[] emptyReferral = new int[numNeighbors];
			referralMap.put(node, emptyReferral);
			return emptyReferral;
		}
	}
	
	/** Set the index'th entry of node's referral array to value.
	 */
	public void setValue(Node node, int index, int value) {
		int[] values = getReferral(node);
		values[index] = value;
	}
	
	/** Add the values of node1's referral array onto the
	 *  referral array of node2.
	 */
	public void addValuesOnto(Node node1, Node node2) {
		int[] node1ref = getReferral(node1);
		int[] node2ref = getReferral(node2);
		for (int i = 0; i < numNeighbors; i++) {
			node2ref[i] = node1ref[i] + node2ref[i];
		}
	}
	
	/** Represent this class by showing the int[] corresponding
	 *  to each of the nodes.
	 */
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
