package dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection;

import java.util.List;

import dbs.pprl.toolbox.lu.blocking.metricSpace.RoutingNode;

public interface DynamicPivotSelector {

	public RoutingNode findPivot(List<RoutingNode> pivots);
}
