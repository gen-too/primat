package dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dbs.pprl.toolbox.lu.blocking.metricSpace.ChildNode;
import dbs.pprl.toolbox.lu.blocking.metricSpace.RoutingNode;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

public class PivotRandomSelector implements StaticPivotSelector{
	private static final int SEED = 42;
	
	private int numberOfPivots;
	private Random rnd;
	
	public PivotRandomSelector(int numberOfPivots) {
		this.numberOfPivots = numberOfPivots;
		this.rnd = new Random(SEED);
	}
	
	@Override
	public List<RoutingNode> findPivots(List<ChildNode> fingerprints) {
		final int[] positions =
			rnd
			.ints(0, fingerprints.size())
			.distinct()
			.limit(this.numberOfPivots)
			.toArray();
		
		final List<RoutingNode> pivots = new ArrayList<>(this.numberOfPivots);
		
		for (final int pos : positions){
			final EncodedRecord pivot = fingerprints.get(pos).getFeatures();
			pivots.add(new RoutingNode(pivot));
		}
		
		return pivots;
	}
}