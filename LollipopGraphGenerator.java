/**
 * A class for generating lollipop graphs.
 */
class LollipopGraphGenerator {
	private LollipopGraphGenerator() { }
	
	static ReadableGraph<Integer> generateAdjListGraph(int n) {
		AdjListGraph graph = new AdjListGraph(n);
		for (int i = n / 2 - 1; i >= 0; i--) {
			for (int j = n / 2 - 1; j >= 0; j--) {
				if (i != j) {
					graph.addEdge(i, j);
				}
			}
		}
		for (int i = n / 2; i < n; i++) {
			graph.addEdge(i - 1, i);
			graph.addEdge(i, i - 1);
		}
		return graph;
	}
}
