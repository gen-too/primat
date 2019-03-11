package dbs.pprl.toolbox.lu.blocking.metricSpace.indexing;

import java.util.List;

import dbs.pprl.toolbox.lu.blocking.metricSpace.ChildNode;
import dbs.pprl.toolbox.lu.blocking.metricSpace.RoutingNode;
import dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection.DynamicPivotSelector;
import dbs.pprl.toolbox.lu.distanceFunctions.DistanceFunction;

//TODO: Does not work properly, see also PivotBy{Entries/Radius/FurthesNode}Selector
public class DynamicIndexer implements IndexMethod {

	private static final double OVERLAP_MAX_VALUE = 0.6;
	
	private DistanceFunction distanceFunction;
	private DynamicPivotSelector pivotSelector;
	
	public DynamicIndexer(DistanceFunction distanceFunction, DynamicPivotSelector pivotSelector){
		this.distanceFunction = distanceFunction;
		this.pivotSelector = pivotSelector;
	}
	
	@Override
	public void indexRecords(List<ChildNode> fingerprints, List<RoutingNode> pivots) {
		long intersection = 0;
		int nbrRecords = 0;
		
		for (final ChildNode record : fingerprints){
			if (!record.getToIgnore()){
				intersection = indexRecord(record, pivots, intersection, nbrRecords);
			}
		}
	}
	
	private long indexRecord(ChildNode record, List<RoutingNode> pivots, long intersection, 
			int nbrRecords){
		double distance = 0;
		RoutingNode nearestPivot = null;	
		
		for(final RoutingNode pivot : pivots){
			if (record == null || pivot == null) throw new RuntimeException("Null");
			
			if(record.getId().equals(pivot.getId())){
				record.setDistanceToParent(0);
				nearestPivot = pivot;
				distance = 0;
				
				if(nearestPivot.getCoveringRadius() == 0){
					nearestPivot.setDistantNode(record);
				}
			}
			else {
				distance = this.distanceFunction.computeDistance(record, pivot);
				
				if(distance < record.getDistanceToParent()){			
					record.setDistanceToParent(distance);
					nearestPivot = pivot;
				}
			}
			
			if(distance <= pivot.getCoveringRadius()) {
				intersection++;
			}
		}
		
		nearestPivot.addEntry(record);
		record.setRouting(nearestPivot);
		
		if (nearestPivot.getCoveringRadius() < record.getDistanceToParent()){
			nearestPivot.setCoveringRadius(record.getDistanceToParent());
			nearestPivot.setDistantNode(record);
		}

		/////////////////////////////////////////////////////////////////
		final double overlapValue = 100.0 * (intersection)/(pivots.size() * nbrRecords);
		
		if(overlapValue > OVERLAP_MAX_VALUE){
			final RoutingNode newPivot = this.pivotSelector.findPivot(pivots);
			if (newPivot != null){
				pivots.add(newPivot);
			}
		}
		/////////////////////////////////////////////////////////////////
		
		return intersection;

	}

}
