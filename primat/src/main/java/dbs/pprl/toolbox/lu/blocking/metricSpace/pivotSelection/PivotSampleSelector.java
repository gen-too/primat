package dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dbs.pprl.toolbox.lu.blocking.metricSpace.ChildNode;
import dbs.pprl.toolbox.lu.blocking.metricSpace.RoutingNode;
import dbs.pprl.toolbox.lu.distanceFunctions.DistanceFunction;

public class PivotSampleSelector implements StaticPivotSelector{
	private static final int SEED = 42;
	
	private int numberOfPivots;
	private int sampleSize;
	private DistanceFunction distanceFunction;
	private Random rnd;
	
	public PivotSampleSelector(int numberOfPivots, int sampleSize, DistanceFunction distanceFunction) {
		this.numberOfPivots = numberOfPivots;
		this.sampleSize = sampleSize;
		this.distanceFunction = distanceFunction;
		this.rnd = new Random(SEED);
	}
	
	@Override
	public List<RoutingNode> findPivots(List<ChildNode> fingerprints) {
		final List<ChildNode> sample = this.getSample(fingerprints);
		
		final int index = rnd.nextInt(this.sampleSize);
		ChildNode expl = sample.get(index);
		
		final List<RoutingNode> pivots = new ArrayList<>(this.numberOfPivots);
		double tmpMaxDist = 0;
		ChildNode candidate = expl;
		
		while (fingerprints.size() < this.numberOfPivots) {
			int candidatePosition = 0;
			for (int sampleIndex = 0; sampleIndex < sample.size(); sampleIndex++){
				final ChildNode record = sample.get(sampleIndex);
				final double distance = this.distanceFunction.computeDistance(expl, record);
				record.setAccumulatedDistance(distance);
				
				if (distance > tmpMaxDist){
					candidate = record;
					candidatePosition = sampleIndex;
					tmpMaxDist = distance;
				}
			}
			
			expl = candidate;
			pivots.add(new RoutingNode(candidate.getFeatures()));
			sample.remove(candidatePosition);
		}
		
		for (final ChildNode node : sample){
			node.resetAccumulatedDistance();
		}
		
		return pivots;
	}	
	
	protected List<ChildNode> getSample(List<ChildNode> fingerprints){
		final int[] indices = 
				this.rnd
				.ints(0, fingerprints.size())
				.distinct()
				.limit(this.sampleSize)
				.toArray();
			
		final List<ChildNode> sample = new ArrayList<>(this.sampleSize);
		
		for (final int index : indices){
			sample.add(fingerprints.get(index));
		}
		
		return sample;
	}
}