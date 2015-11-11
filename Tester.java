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
		run(3, nodeS, false);
	}
	
	public static void run(int k, Node s, boolean cumulative) {
		Set<Node> A = new HashSet<Node>();
		Queue<Node> Q = new LinkedList<Node>();
		Set<Node> C = new HashSet<Node>();
		int neighborCount = s.getAdj().size();
		
		ReferralLog referralLog = new ReferralLog(neighborCount);
		
		for (int i = 0; i < s.getAdj().size(); i++) {
			Node v = s.getAdj().get(i);
			referralLog.setValue(v, i, 1);
			C.add(v);
		}
		A.add(s);
		
		for (int i = 0; i < k-1; i++) {
			A.addAll(C);
			Q.addAll(C);
			C.clear();
			
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
		
		double[] p = new double[neighborCount];
		for (Node v : C) {
			int[] referrals = referralLog.getReferral(v);
			for (int i = 0; i < referrals.length; i++) {
				int sum = sum(referrals);
				p[i] += referrals[i] / (double) sum;
			}
		}
		
		for (int i = 0; i < p.length; i++) {
			System.out.println(p[i]);
		}
	}
	
	private static int sum(int[] array) {
		int sum = 0;
		for (int entry : array) {
			sum += entry;
		}
		return sum;
	}


}
