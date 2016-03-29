package graph;

import java.util.List;
import java.util.stream.Stream;

import matchings.Matching;

public class ReadableMatchingGraph extends ReadableGraph<Matching>{

	@Override @Deprecated
	public int getNodeCnt() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Matching> getNeighbors(Matching node) {
		return node.getNeighborMatchings();
	}

	@Override @Deprecated
	public List<Matching> getInboundNodes(Matching id) {
		throw new UnsupportedOperationException();
	}

	@Override @Deprecated
	public ReadableNode<Matching> getNode(Matching i) {
		throw new UnsupportedOperationException();
	}

	@Override @Deprecated
	public Stream<? extends ReadableNode<Matching>> getNodes() {
		throw new UnsupportedOperationException();
	}

}
