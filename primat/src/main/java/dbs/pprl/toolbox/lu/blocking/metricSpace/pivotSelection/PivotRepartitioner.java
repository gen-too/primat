package dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection;

import java.util.Iterator;
import java.util.List;

import dbs.pprl.toolbox.lu.blocking.metricSpace.ChildNode;
import dbs.pprl.toolbox.lu.blocking.metricSpace.RoutingNode;
import dbs.pprl.toolbox.lu.distanceFunctions.DistanceFunction;

public class PivotRepartitioner {

	public static void repartitionEntriesFromAllPivots(List<RoutingNode> pivotSet, RoutingNode newPivot,
			DistanceFunction distFunction){
		
		for(RoutingNode pivot : pivotSet){
			pivot.setCoveringRadius(0);
			pivot.setDistantNode(null);
			final Iterator<ChildNode> iterator = pivot.getEntries().iterator();
			
			while(iterator.hasNext()){
				final ChildNode child = iterator.next();
				final double distance = distFunction.computeDistance(child, newPivot);
				
				if(distance < child.getDistanceToParent()){
					child.setDistanceToParent(distance);
					child.setRouting(newPivot);
					iterator.remove();
					newPivot.addEntry(child);
					if(newPivot.getCoveringRadius() < distance){
						newPivot.setCoveringRadius(distance);
						newPivot.setDistantNode(child);
					}
				}
				else{
					if(pivot.getCoveringRadius() < child.getDistanceToParent()){
						pivot.setCoveringRadius(child.getDistanceToParent());
						pivot.setDistantNode(child);
					}
				}
			}
		}
	}
}