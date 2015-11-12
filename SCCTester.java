import java.util.Iterator;
import java.util.Stack;

public class SCCTester {

	public static boolean isStronglyConnected(Graph g) {
		
		Iterator<Node> iter1 = g.getDFSIterator();
		while (iter1.hasNext()) {
			Node v = iter1.next();
			
			Iterator<Node> iter2 = g.getDFSIterator(v);
			int counter = 0;
			while (iter2.hasNext()) {
				iter2.next();
				counter++;
			}
			if (counter < g.getNodeCnt()) return false;
		}
		
		return true;
	}

}
