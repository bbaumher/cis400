package graph;

public interface GraphGenerator {
	
	public AdjListGraph generateAdjListGraph(int n, double d, boolean directed);
	
	public AdjMatrixGraph generateAdjMatrixGraph(int n, double d, boolean directed
  );

}
