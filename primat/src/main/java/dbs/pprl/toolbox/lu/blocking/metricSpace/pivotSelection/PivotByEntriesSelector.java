package dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection;

import java.util.List;

import dbs.pprl.toolbox.lu.blocking.metricSpace.RoutingNode;
import dbs.pprl.toolbox.lu.distanceFunctions.DistanceFunction;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

public class PivotByEntriesSelector implements DynamicPivotSelector{	
	
	private DistanceFunction distanceFunction;
	
	public PivotByEntriesSelector(DistanceFunction distanceFunction) {
		this.distanceFunction = distanceFunction;
	}
	
	@Override
	public RoutingNode findPivot(List<RoutingNode> pivots) {
		RoutingNode newPivot = null;
		RoutingNode bestPivot= pivots.get(0);
		
		for (RoutingNode pivot : pivots) {
			if (pivot.getEntries().size() > bestPivot.getEntries().size()) {
				bestPivot = pivot;
			}	
		}
		
		if (bestPivot.getDistantNode() != null) {
			final EncodedRecord rec = bestPivot.getDistantNode().getFeatures();
			newPivot = new RoutingNode(rec);
			PivotRepartitioner.repartitionEntriesFromAllPivots(pivots, newPivot, this.distanceFunction);
		}
		// treatment of else case
		
		return newPivot;
	}
}