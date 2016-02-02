package graph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ProbabilityDistributionAlgorithm {
	/**
	 * The k credit algorithm.
	 * 
	 * @param s the starting node
	 * @param k we consider nodes up to k away from the starting node
	 * @param creditCalculator The function used to compute the transition
	 * probabilities after the traversal is complete.
	 * @return 
	 */
	public static Map<ReadableNode<?>, Double> getNeighborVector(
		ReadableNode<?> s,
		int k,
		CreditCalculator creditCalculator)
	{
		Set<ReadableNode<?>> seenNodes = new HashSet<>(); //the nodes that no longer need to be considered
		Queue<ReadableNode<?>> nodeQueue = new LinkedList<>(); //the nodes on the current layer of the BFS
		Set<ReadableNode<?>> pendingNodes = new HashSet<>(); //the the nodes being discovered on the edge of the BFS
		List<ReadableNode<?>> adjList = new ArrayList<>(); //the number of neighbors of the starting node
		adjList.addAll(s.getAdjSet());
		ArrayList<Set<ReadableNode<?>>> nodeTiers = new ArrayList<>(); //a list of sets of nodes, each set containing nodes i away from s
		ReferralLog referralLog = new ReferralLog(adjList); //a map from each node to its referral array
    
    //give each of s's neighbors a 1 in its corresponding referral array
    for (ReadableNode<?> v : adjList) {
      referralLog.setValue(v, v, 1);
      pendingNodes.add(v);
    }
		
		//perform logistics on s as the zeroth tier
		seenNodes.add(s);
		Set<ReadableNode<?>> zerothTier = new HashSet<>();
		zerothTier.add(s);
		nodeTiers.add(zerothTier);
		
		//perform a BFS, filling out the nodeTiers and keeping track of referrals
		for (int i = 0; i < k-1; i++) {
			seenNodes.addAll(pendingNodes); //archive the nodes seen last round
			nodeQueue.addAll(pendingNodes); //the nodes discovered last round are next
			nodeTiers.add(pendingNodes); //add the pending nodes to the nodeTiers
			pendingNodes = new HashSet<>(); //reset the pending nodes
			
			//iterate through each node in the queue
			while (!nodeQueue.isEmpty()) {
				ReadableNode<?> v = nodeQueue.poll();
				
				//examine each of the node's neighbors
				for (
          Iterator<? extends ReadableNode<?>> iterator =
            v.getAdjStream().iterator();
          iterator.hasNext();
          )
        {
          ReadableNode<?> u = iterator.next();
					if (seenNodes.contains(u)) {
          } //if we've seen it before, skip it
          else {//otherwise, we haven't seen it yet
						pendingNodes.add(u); //add it to the pending nodes (possibly redundantly)
						referralLog.addValuesOnto(v,u); //propagate the referrals
					}
				}
			}
		}
		
		//add the final tier to the nodeTiers
		nodeTiers.add(pendingNodes);
		
		//perform and return some algorithm to determine credits
		double[] p =
			creditCalculator.apply(nodeTiers, referralLog, adjList.size(), k);
		
		Map<ReadableNode<?>, Double> result = new HashMap<>(adjList.size());
		int index = 0;
		for (ReadableNode<?> node : adjList) {
			result.put(node, p[index++]);
		}
		return result;
	}
	
	/**
	 * The k credit algorithm.
	 * 
	 * @param graph the graph on which to run the algorithm
	 * @param k we consider nodes up to k away from the starting node
	 * @param creditCalculator The function used to compute the transition
	 * probabilities after the traversal is complete.
	 * @return A {@link TransitionMatrix} for the Markov chain.
	 */
	static TransitionMatrix getTransitionMatrix(
		ReadableGraph<?> graph,
		int k,
		CreditCalculator creditCalculator)
	{
		return
			TransitionMatrix.fromProbabilityRetriever(
				graph.getNodes(),
				source -> {
					Map<ReadableNode<?>, Double> map =
						getNeighborVector(source, k, creditCalculator);
					return target -> map.getOrDefault(target, 0d);
				}
			);
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
	static double[] calculateCredits(ArrayList<Set<ReadableNode<?>>> nodeTiers,
			ReferralLog referralLog, int neighborCount, int k) {
		double[] p = new double[neighborCount]; //instantiate the output array
		Set<ReadableNode<?>> edgeNodes = nodeTiers.get(k); //get the final tier (i.e. nodes exactly k away from s)
		
		//each node has 1 credit, which is split proportionally to its referrals
		for (ReadableNode<?> v : edgeNodes) {
			int[] referrals = referralLog.getReferral(v);
			for (int i = 0; i < referrals.length; i++) {
				double sum = sum(referrals);
				p[i] += referrals[i] / sum;
			}
		}
		normalize(p); //normalize the probability vector
		return p;
	}
	
	/** All tiers give out credits.
	 */
	static double[] calculateCredits2(ArrayList<Set<ReadableNode<?>>> nodeTiers,
			ReferralLog referralLog, int neighborCount, int k) {
		double[] p = new double[neighborCount]; //instantiate the output array
		
		for (int t = 1; t <= k; t++) {
			Set<ReadableNode<?>> tier = nodeTiers.get(t); //get the each tier (i.e. nodes i away from s)
			//each node has 1 credit, which is split proportionally to its referrals
			for (ReadableNode<?> v : tier) {
				int[] referrals = referralLog.getReferral(v);
				for (int i = 0; i < referrals.length; i++) {
					double sum = sum(referrals);
					p[i] += referrals[i] / sum;
				}
			}
		}
		normalize(p); //normalize the probability vector
		return p;
	}
	
	/** All tiers give out credits, but proportionally to their distance away.
	 */
	static double[] calculateCredits3(ArrayList<Set<ReadableNode<?>>> nodeTiers,
			ReferralLog referralLog, int neighborCount, int k) {
		double[] p = new double[neighborCount]; //instantiate the output array
		
		for (int t = 1; t <= k; t++) {
			Set<ReadableNode<?>> tier = nodeTiers.get(t); //get the each tier (i.e. nodes i away from s)
			//each node has 1 credit, which is split proportionally to its referrals
			for (ReadableNode<?> v : tier) {
				int[] referrals = referralLog.getReferral(v);
				for (int i = 0; i < referrals.length; i++) {
					double sum = sum(referrals);
					p[i] += referrals[i] / sum * t;
				}
			}
		}
		normalize(p); //normalize the probability vector
		return p;
	}
	
	/** A control algorithm. It's a standard random walk.
	 */
	static double[] calculateCredits4(ArrayList<Set<ReadableNode<?>>> nodeTiers,
			ReferralLog referralLog, int neighborCount, int k) {
		double[] p = new double[neighborCount]; //instantiate the output array
		normalize(p); //normalize the probability vector
		return p;
	}

  static CreditCalculator getSimpleMixedCalculator(double longDistanceWeight) {
    return
      (nodeTiers, referralLog, neighborCount, k) -> {
        double[] result =
          calculateCredits(nodeTiers, referralLog, neighborCount, k);
        for (int i = result.length - 1; i >= 0; i--) {
          result[i] =
            result[i] * longDistanceWeight
              + (1d - longDistanceWeight) / result.length;
        }
        return result;
      };
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
	public static void normalize(double[] array) {
		double sum = sum(array);
		for (int i = 0; i < array.length; i++) {
			if (sum == 0) array[i] = 1.0 / array.length;
			else array[i] /= sum;
		}
	}

	/**
	 * An interface for functions that can be used in {@link #getNeighborVector}
	 * to produce probability distributions using the k-away system.
	 */
	@FunctionalInterface
	public static interface CreditCalculator {
		double[] apply(
			ArrayList<Set<ReadableNode<?>>> nodeTiers,
			ReferralLog referralLog,
			int neighborCount,
			int k);
	}

  private ProbabilityDistributionAlgorithm() {
  }
}
