package dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection;

import java.util.List;

import dbs.pprl.toolbox.lu.blocking.metricSpace.ChildNode;
import dbs.pprl.toolbox.lu.blocking.metricSpace.RoutingNode;

public interface StaticPivotSelector {

	public List<RoutingNode> findPivots(List<ChildNode> fingerprints);
}
