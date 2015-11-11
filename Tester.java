import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Tester {
	

	public static void main(String[] args) {
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
		AdjListGraph testGraph = new AdjListGraph(12);
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
		
		Node nodeS = testGraph.getNode(s);
		run(3, nodeS);
	}
	
	public static void run(int k, Node s) {
		Set<Node> A = new HashSet<Node>();
		Queue<Node> Q = new LinkedList<Node>();
		Set<Node> C = new HashSet<Node>();
		int neighborCount = s.getAdj().size();
		ArrayList<Set<Node>> nodeTiers = new ArrayList<Set<Node>>();
		ReferralLog referralLog = new ReferralLog(neighborCount);
		
		for (int i = 0; i < s.getAdj().size(); i++) {
			Node v = s.getAdj().get(i);
			referralLog.setValue(v, i, 1);
			C.add(v);
		}
		
		A.add(s);
		Set<Node> zerothTier = new HashSet<Node>();
		zerothTier.add(s);
		nodeTiers.add(zerothTier);
		
		for (int i = 0; i < k-1; i++) {
			A.addAll(C);
			Q.addAll(C);
			nodeTiers.add(C);
			C = new HashSet<Node>();
			
			while (!Q.isEmpty()) {
				Node v = Q.poll();
				for (Node u : v.getAdj()) {
					if (A.contains(u)) continue;
					else {
						C.add(u);
						referralLog.addValuesOnto(v,u);
					}
				}
			}
		}
		
		nodeTiers.add(C);
		
		double[] p = calculateCredits(nodeTiers, referralLog, neighborCount, k);
		
		for (int i = 0; i < p.length; i++) {
			System.out.println(p[i]);
		}
	}
	
	private static double[] calculateCredits(ArrayList<Set<Node>> nodeTiers, 
			ReferralLog referralLog, int neighborCount, int k) {
		double[] p = new double[neighborCount];
		Set<Node> edgeNodes = nodeTiers.get(k);
		for (Node v : edgeNodes) {
			int[] referrals = referralLog.getReferral(v);
			for (int i = 0; i < referrals.length; i++) {
				int sum = sum(referrals);
				p[i] += referrals[i] / (double) sum;
			}
		}
		return p;
	}
	
	private static int sum(int[] array) {
		int sum = 0;
		for (int entry : array) {
			sum += entry;
		}
		return sum;
	}


}
