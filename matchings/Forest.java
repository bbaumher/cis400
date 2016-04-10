package matchings;

import java.util.ArrayList;

public class Forest {
	
	protected ArrayList<Tree> treeList;
	
	public Forest() {
		treeList = new ArrayList<Tree>();
	}
	
	public void addNewTree(Node node) {
		Tree tree = new Tree(node);
		treeList.add(tree);
	}

	
}
