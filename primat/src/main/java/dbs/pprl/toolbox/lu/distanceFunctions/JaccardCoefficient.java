package dbs.pprl.toolbox.lu.distanceFunctions;

import java.util.BitSet;

import dbs.pprl.toolbox.lu.blocking.metricSpace.Node;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.lu.similarityFunctions.JaccardSimilarity;

public class JaccardCoefficient implements DistanceFunction{

	@Override
	public double computeDistance(Node node1, Node node2) {
		final EncodedRecord rec1 = node1.getFeatures();
		final EncodedRecord rec2 = node2.getFeatures();
		
		final BitSet bs1 = rec1.getBitVectors().get(0);
		final BitSet bs2 = rec2.getBitVectors().get(0);
		
		final JaccardSimilarity jaccard = new JaccardSimilarity();
		final double jaccardSim = jaccard.calculateSimilarity(bs1, bs2);
		return 1 - jaccardSim;
	}	
}