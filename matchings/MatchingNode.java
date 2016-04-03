package matchings;

public class MatchingNode extends Node {
	
	protected final Matching myMatching;

	public MatchingNode(Matching matching) {
		super(matching.toString());
		myMatching = matching;
	}
	
	public Matching getMatching() {
		return myMatching;
	}
	
	@Override
	public String toString() {
		return myMatching.toString2();
	}

	public int compareTo(MatchingNode o) {
		return this.myMatching.compareTo(o.myMatching);
	}
	
	@Override
	public int hashCode() {
		return myMatching.hashCode();
	}

}
