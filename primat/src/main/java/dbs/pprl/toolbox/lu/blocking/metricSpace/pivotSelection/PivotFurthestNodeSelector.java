package dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection;

import java.util.List;

import dbs.pprl.toolbox.lu.blocking.metricSpace.ChildNode;
import dbs.pprl.toolbox.lu.blocking.metricSpace.RoutingNode;
import dbs.pprl.toolbox.lu.distanceFunctions.DistanceFunction;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

public class PivotFurthestNodeSelector implements DynamicPivotSelector{	
	
	private DistanceFunction distanceFunction;
	
	public PivotFurthestNodeSelector(DistanceFunction distanceFunction) {
		this.distanceFunction = distanceFunction;
	}
	
	@Override
	public RoutingNode findPivot(List<RoutingNode> pivots) {
		double accDistance = 0;
		ChildNode bestNode = null;
		
		for(RoutingNode pivot1 : pivots){
			final ChildNode candidate = pivot1.getDistantNode();
			if(candidate != null){
				candidate.resetAccumulatedDistance();
				
				for(RoutingNode pivot2 : pivots){
					final double distance = this.distanceFunction.computeDistance(candidate, pivot2);
					candidate.setAccumulatedDistance(distance);
				}
				
				if(candidate.getAccumulatedDistance() > accDistance){
					accDistance = candidate.getAccumulatedDistance();
					bestNode = candidate;
				}
			}
			
			candidate.resetAccumulatedDistance();
		}
		
		final EncodedRecord bestRec = bestNode.getFeatures();	
		return new RoutingNode(bestRec);
	}
}