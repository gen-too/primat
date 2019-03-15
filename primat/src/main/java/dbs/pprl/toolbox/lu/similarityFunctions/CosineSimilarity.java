package dbs.pprl.toolbox.lu.similarityFunctions;

import java.util.BitSet;


import dbs.pprl.toolbox.utils.BitSetUtils;

public class CosineSimilarity extends SimilarityFunction{

	// = |A AND B| / (SQRT(|A| * |B|) ?
	@Override
	public double calculateSim(BitSet left, BitSet right) {	
		final BitSet and = BitSetUtils.and(left, right);
		final double dotProduct = and.cardinality();
		
		final double leftL2Norm = Math.sqrt(left.cardinality()); 
		final double rightL2Norm = Math.sqrt(right.cardinality());
		
		final double cosineSim = dotProduct / (leftL2Norm * rightL2Norm);
		return cosineSim;
	}	
}