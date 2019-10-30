package dbs.pprl.toolbox.lu.distanceFunctions;

import java.util.BitSet;

import dbs.pprl.toolbox.lu.blocking.metricSpace.Node;

public class NormalizedHammingDistance extends HammingDistance{

	@Override
	public double computeDistance(Node node1, Node node2) {
		final double hammingDistance = super.computeDistance(node1, node2);
		
		final BitSet bs1 = node1.getFeatures().getBitVectors().get(0);
		final BitSet bs2 = node2.getFeatures().getBitVectors().get(0);
		
		final double maxLength = Math.max(bs1.cardinality(), bs2.cardinality());
		
		return hammingDistance / maxLength;
	}	
}