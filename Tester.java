import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Tester {
	
	/** Run the algorithm on a test graph.
	 */
	public static void main(String[] args) {
		//create nodes
		int s = 0;
		int a = 1;
		int b = 2;
		int c = 3;
		int d = 4;
		int e = 5;
		int f = 6;
		int g = 7;
		int h = 8;
		int i = 9;
		int j = 10;
		int k = 11;
		
		//create graph
		AdjListGraph testGraph = new AdjListGraph(12);
		
		//add edges to the graph
		testGraph.addEdge(s,a);
		testGraph.addEdge(s,b);
		testGraph.addEdge(s,c);
		testGraph.addEdge(s,d);
		testGraph.addEdge(a,e);
		testGraph.addEdge(a,g);
		testGraph.addEdge(b,e);
		testGraph.addEdge(b,f);
		testGraph.addEdge(c,d);
		testGraph.addEdge(c,h);
		testGraph.addEdge(c,i);
		testGraph.addEdge(d,h);
		testGraph.addEdge(e,j);
		testGraph.addEdge(g,j);
		testGraph.addEdge(i,k);
		
		//run the algorithm
		Node nodeS = testGraph.getNode(s);
		runAlgorithm(nodeS, 3);
	}
	
	/**
	 * The k credit algorithm.
	 * 
	 * @param s the starting node
	 * @param k we consider nodes up to k away from the starting node
	 */
	public static void runAlgorithm(Node s, int k) {
		Set<Node> seenNodes = new HashSet<Node>(); //the nodes that no longer need to be considered
		Queue<Node> nodeQueue = new LinkedList<Node>(); //the nodes on the current layer of the BFS
		Set<Node> pendingNodes = new HashSet<Node>(); //the the nodes being discovered on the edge of the BFS
		int neighborCount = s.getAdj().size(); //the number of neighbors of the starting node
		ArrayList<Set<Node>> nodeTiers = new ArrayList<Set<Node>>(); //a list of sets of nodes, each set containing nodes i away from s
		ReferralLog referralLog = new ReferralLog(neighborCount); //a map from each node to its referral array
		
		//give each of s's neighbors a 1 in its corresponding referral array
		for (int i = 0; i < s.getAdj().size(); i++) {
			Node v = s.getAdj().get(i);
			referralLog.setValue(v, i, 1);
			pendingNodes.add(v);
		}
		
		//perform logistics on s as the zeroth tier
		seenNodes.add(s);
		Set<Node> zerothTier = new HashSet<Node>();
		zerothTier.add(s);
		nodeTiers.add(zerothTier);
		
		//perform a BFS, filling out the nodeTiers and keeping track of referrals
		for (int i = 0; i < k-1; i++) {
			seenNodes.addAll(pendingNodes); //archive the nodes seen last round
			nodeQueue.addAll(pendingNodes); //the nodes discovered last round are next
			nodeTiers.add(pendingNodes); //add the pending nodes to the nodeTiers
			pendingNodes = new HashSet<Node>(); //reset the pending nodes
			
			//iterate through each node in the queue
			while (!nodeQueue.isEmpty()) {
				Node v = nodeQueue.poll();
				
				//examine each of the node's neighbors
				for (Node u : v.getAdj()) {
					if (seenNodes.contains(u)) continue; //if we've seen it before, skip it
					else {//otherwise, we haven't seen it yet
						pendingNodes.add(u); //add it to the pending nodes (possibly redundantly)
						referralLog.addValuesOnto(v,u); //propagate the referrals
					}
				}
			}
		}
		
		//add the final tier to the nodeTiers
		nodeTiers.add(pendingNodes);
		
		//perform some algorithm to determine credits
		double[] p = calculateCredits(nodeTiers, referralLog, neighborCount, k);
		
		//output the display
		for (int i = 0; i < p.length; i++) {
			System.out.println(p[i]);
		}
	}
	
	/**
	 * Perform the k credit algorithm.
	 * 
	 * @param nodeTiers a list of sets of nodes, each set containing nodes i away from s
	 * @param referralLog a map from each node to its referral array
	 * @param neighborCount the number of neighbors of the starting node
	 * @param k we consider nodes up to k away from the starting node
	 * @return the probability distribution for going to s's neighbors
	 */
	private static double[] calculateCredits(ArrayList<Set<Node>> nodeTiers, 
			ReferralLog referralLog, int neighborCount, int k) {
		double[] p = new double[neighborCount]; //instantiate the output array
		Set<Node> edgeNodes = nodeTiers.get(k); //get the final tier (i.e. nodes exactly k away from s)
		
		//each node has 1 credit, which is split proportionally to its referrals
		for (Node v : edgeNodes) {
			int[] referrals = referralLog.getReferral(v);
			for (int i = 0; i < referrals.length; i++) {
				double sum = sum(referrals);
				p[i] += referrals[i] / sum;
			}
		}
		normalize(p); //normalize the probability vector
		return p;
	}
	
	/** Return the sum of an integer array.
	 */
	private static int sum(int[] array) {
		int sum = 0;
		for (int entry : array) {
			sum += entry;
		}
		return sum;
	}
	
	/** Return the sum of a double array.
	 */
	private static double sum(double[] array) {
		double sum = 0;
		for (double entry : array) {
			sum += entry;
		}
		return sum;
	}
	
	/** Normalize an array by scaling each of its entries so that
	 *  the sum becomes 1. Modifies the array in place.
	 */
	private static void normalize(double[] array) {
		double sum = sum(array);
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i] / sum;
		}
	}


}
