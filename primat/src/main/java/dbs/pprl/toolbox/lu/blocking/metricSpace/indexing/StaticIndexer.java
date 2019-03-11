package dbs.pprl.toolbox.lu.blocking.metricSpace.indexing;

import java.util.List;

import dbs.pprl.toolbox.lu.blocking.metricSpace.ChildNode;
import dbs.pprl.toolbox.lu.blocking.metricSpace.RoutingNode;
import dbs.pprl.toolbox.lu.distanceFunctions.DistanceFunction;

public class StaticIndexer implements IndexMethod {

	private DistanceFunction distanceFunction;
	
	public StaticIndexer(DistanceFunction distanceFunction){
		this.distanceFunction = distanceFunction;
	}
	
	@Override
	public void indexRecords(List<ChildNode> fingerprints, List<RoutingNode> pivots) {		
		for (final ChildNode record : fingerprints){
			this.indexRecord(record, pivots);
		}
	}
	
	private void indexRecord(ChildNode record, List<RoutingNode> pivots){
		RoutingNode nearestPivot = null;
		for (RoutingNode pivot : pivots){
			if (record == null || pivot == null) throw new RuntimeException("Null");
			
			if (record.getFeatures().getId().equals(pivot.getFeatures().getId())){
				record.setDistanceToParent(0);
				nearestPivot = pivot;
				
				if (nearestPivot.getCoveringRadius() == 0){
					nearestPivot.setDistantNode(record);
				}
			}
			else{
				final double distance = distanceFunction.computeDistance(record, pivot);
				
				if (distance < record.getDistanceToParent()){
					record.setDistanceToParent(distance);
					nearestPivot = pivot;
				}
			}
		}
		if (nearestPivot != null){
			nearestPivot.addEntry(record);
			record.setRouting(nearestPivot);
			
			if (nearestPivot.getCoveringRadius() < record.getDistanceToParent()){
				nearestPivot.setDistantNode(record);
				nearestPivot.setCoveringRadius(record.getDistanceToParent());
			}
		}
	}
}