package dbs.pprl.toolbox.lu.distanceFunctions;

import java.util.BitSet;

import dbs.pprl.toolbox.lu.blocking.metricSpace.Node;
import dbs.pprl.toolbox.utils.BitSetUtils;

public class HammingDistance implements DistanceFunction{

	@Override
	public double computeDistance(Node node1, Node node2) {
		final BitSet bs1 = node1.getFeatures().getBitVector();
		final BitSet bs2 = node2.getFeatures().getBitVector();
		
		final BitSet xor = BitSetUtils.xor(bs1, bs2);
		return xor.cardinality();
	}	
}