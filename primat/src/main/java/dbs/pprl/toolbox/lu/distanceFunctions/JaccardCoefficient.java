package dbs.pprl.toolbox.lu.distanceFunctions;

import dbs.pprl.toolbox.lu.blocking.metricSpace.Node;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.lu.similarityCalculation.JaccardSimilarityCalculator;

public class JaccardCoefficient implements DistanceFunction{

	@Override
	public double computeDistance(Node node1, Node node2) {
		final EncodedRecord rec1 = node1.getFeatures();
		final EncodedRecord rec2 = node2.getFeatures();
		
		final double jaccardSim = JaccardSimilarityCalculator.calculateJaccardSimilarity(rec1, rec2);
		return 1 - jaccardSim;
	}	
}