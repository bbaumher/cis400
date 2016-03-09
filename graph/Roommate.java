package graph;

public class Roommate {
	
	public enum sociality {INTROVERT, EXTROVERT};
	public enum bedtime {EARLY, MEDIUM, LATE};
	public enum messiness {CLEAN, MEDIUM, MESSY};
	public enum sharing {SHARING, NOSHARING};
	
	public sociality mySociality;
	public bedtime myBedtime;
	public messiness myMessiness;
	public sharing mySharing;
	
	public Roommate() {
		assignRandomAttributes();
	}

	private void assignRandomAttributes() {
		double rand = Math.random();
		mySociality = rand < 1.0/2 ? sociality.INTROVERT : sociality.EXTROVERT;
		rand = Math.random();
		myBedtime = rand < 1.0/3 ? bedtime.EARLY : rand < 2.0/3 ? bedtime.MEDIUM : bedtime.LATE;
		rand = Math.random();
		myMessiness = rand < 1.0/3 ? messiness.CLEAN : rand < 2.0/3 ? messiness.MEDIUM : messiness.MESSY;
		rand = Math.random();
		mySharing = rand < 1.0/2 ? sharing.SHARING : sharing.NOSHARING;
	}
	
	public boolean isCompatible(Roommate other) {
		return getCompatibilityScore(other) >= 3;
	}
	
	public int getCompatibilityScore(Roommate other) {
		int compatibilityScore = 0;
		if (mySociality == other.mySociality) compatibilityScore++;
		if (myBedtime == other.myBedtime) compatibilityScore++;
		if (myMessiness == other.myMessiness) compatibilityScore++;
		if (mySharing == other.mySharing) compatibilityScore++;
		return compatibilityScore;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("mySociality: " + mySociality + "\n");
		buf.append("myBedtime: " + myBedtime + "\n");
		buf.append("myMessiness: " + myMessiness + "\n");
		buf.append("mySharing: " + mySharing);
		return buf.toString();
	}
}
