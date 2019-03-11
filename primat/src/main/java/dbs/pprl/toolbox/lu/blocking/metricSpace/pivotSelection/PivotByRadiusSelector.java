package dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection;

import java.util.List;

import dbs.pprl.toolbox.lu.blocking.metricSpace.RoutingNode;
import dbs.pprl.toolbox.lu.distanceFunctions.DistanceFunction;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

public class PivotByRadiusSelector implements DynamicPivotSelector{	
	
	private DistanceFunction distanceFunction;
	
	public PivotByRadiusSelector(DistanceFunction distanceFunction) {
		this.distanceFunction = distanceFunction;
	}
	
	@Override
	public RoutingNode findPivot(List<RoutingNode> pivots) {
		RoutingNode bestPivot = pivots.get(0);
		
		for(RoutingNode pivot : pivots){
			if(pivot.getCoveringRadius() > bestPivot.getCoveringRadius()){
				bestPivot = pivot;
			}
		}
		
		RoutingNode newPivot = null;
		
		if(bestPivot.getDistantNode() != null){
			final EncodedRecord distRec = bestPivot.getDistantNode().getFeatures();
			newPivot = new RoutingNode(distRec);
			PivotRepartitioner.repartitionEntriesFromAllPivots(pivots, newPivot, this.distanceFunction);
		}
		
		return newPivot;
	}
}