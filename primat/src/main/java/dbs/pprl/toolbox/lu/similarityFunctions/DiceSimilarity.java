package dbs.pprl.toolbox.lu.similarityFunctions;

import java.util.BitSet;

import dbs.pprl.toolbox.utils.BitSetUtils;

/**
 * 
 * @author mfranke
 *
 */
public final class DiceSimilarity extends SimilarityFunction {
		
	// Dice = 2*Jaccard / (1 + Jaccard)
	// Semimetric (does not fulfill the triangle inequality)
	@Override	
	public double calculateSim(BitSet left, BitSet right){		
		final int card1 = left.cardinality();
		final int card2 = right.cardinality();
		final int and = BitSetUtils.and(left, right).cardinality();	
		
		final double nom = (double) (2 * and);
		final double divisor = (double) (card1 + card2); 
		
		return nom / divisor;
	}
	
}