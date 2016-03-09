package graph;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		CustomizableGraph<Roommate> graph = new CustomizableGraph<Roommate>();
		for (int i = 0; i < 10; i++) {
			Roommate roommate = new Roommate();
			graph.addNode(roommate);
		}
		
		Object[] nodes = graph.getNodes().toArray();
		for (int i = 0; i < nodes.length; i++) {
			for (int j = i+1; j < nodes.length; j++) {
				Node<Roommate> n1 = (Node<Roommate>)nodes[i];
				Node<Roommate> n2 = (Node<Roommate>)nodes[j];
				Roommate r1 = n1.getId();
				Roommate r2 = n2.getId();
				if (r1.isCompatible(r2)) {
					graph.addUndirectedEdge(n1, n2);
				}
			}
		}
	}

}
