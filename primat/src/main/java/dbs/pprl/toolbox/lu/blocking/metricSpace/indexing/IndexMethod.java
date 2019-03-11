package dbs.pprl.toolbox.lu.blocking.metricSpace.indexing;

import java.util.List;

import dbs.pprl.toolbox.lu.blocking.metricSpace.ChildNode;
import dbs.pprl.toolbox.lu.blocking.metricSpace.RoutingNode;

public interface IndexMethod {

	public void indexRecords(List<ChildNode> fingerprints, List<RoutingNode> pivots);
}
