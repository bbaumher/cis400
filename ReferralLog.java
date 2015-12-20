import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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
	// A Map from the neighbors to the index of that node in the int[]s.
	private final Map<ReadableNode, Integer> neighborToIndexMap = new HashMap<>();
	Map<ReadableNode,int[]> referralMap; //the actual mapping from Node to int[]
	
	/** Construct a ReferralLog by storing the neighbors and
	 *  instantiating the map.
	 * 
	 * @param neighbors the neighbors of s
	 */
	public ReferralLog (List<ReadableNode> neighbors) {
		IntStream.range(0, neighbors.size())
			.forEach(
				index -> neighborToIndexMap.put(neighbors.get(index), index));
		referralMap = new HashMap<>();
	}
	
	/** Get the referral of a node, i.e. its mapped to int[].
	 *  If it doesn't have one yet, then an empty one is created,
	 *  added to the mapping, and returned.
	 */
	public int[] getReferral(ReadableNode node) {
		if (referralMap.containsKey(node)) { //node already has a referral array
			return referralMap.get(node);
		}
		else { //node doesn't have a referral array yet
			int[] emptyReferral = new int[neighborToIndexMap.size()];
			referralMap.put(node, emptyReferral);
			return emptyReferral;
		}
	}
	
	/** 
	 * Set the entry of node's referral array corresponding to centralNeighbor.
	 */
	public void setValue(
    ReadableNode fringeNode,
    ReadableNode centralNeighbor,
    int value)
  {
		int[] values = getReferral(fringeNode);
		values[neighborToIndexMap.get(centralNeighbor)] = value;
	}
	
	/** Add the values of node1's referral array onto the
	 *  referral array of node2.
	 */
	public void addValuesOnto(ReadableNode node1, ReadableNode node2) {
		int[] node1ref = getReferral(node1);
		int[] node2ref = getReferral(node2);
		for (int i = 0; i < neighborToIndexMap.size(); i++) {
			node2ref[i] = node1ref[i] + node2ref[i];
		}
	}
	
	/** Represent this class by showing the int[] corresponding
	 *  to each of the nodes.
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		for (ReadableNode v : referralMap.keySet()) {
			buf.append(v).append(" = ");
			for (int value : getReferral(v)) {
				buf.append(value).append(" ");
			}
			buf.append("\n");
		}
		return buf.toString();
	}

}
