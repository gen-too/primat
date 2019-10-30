package dbs.pprl.toolbox.lu.similarityFunctions;

import java.util.BitSet;

import dbs.pprl.toolbox.utils.BitSetUtils;

/**
 * 
 * @author mfranke
 *
 */
public class JaccardSimilarity extends SimilarityFunction{

	// Jaccard = Dice / (2 - Dice)
	// Metric
	@Override
	protected double calculateSim(BitSet left, BitSet right) {
		final int card1 = left.cardinality();
		final int card2 = right.cardinality();
		final int and = BitSetUtils.and(left, right).cardinality();	

		final double nom = (double) and;
		final double divisor = (double) (card1 + card2 - and); 
		
		return nom / divisor;
	}

}