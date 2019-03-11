package dbs.pprl.toolbox.lu.distanceFunctions;

import dbs.pprl.toolbox.lu.blocking.metricSpace.Node;

public interface DistanceFunction {

	public double computeDistance(Node node1, Node node2);
}
