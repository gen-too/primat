package dbs.pprl.toolbox.lu.similarityFunctions;

import java.util.BitSet;

import dbs.pprl.toolbox.utils.BitSetUtils;

public final class BraunBlanquetSimilarity extends SimilarityFunction{

	@Override
	public double calculateSim(BitSet left, BitSet right) {		
		final int card1 = left.cardinality();
		final int card2 = right.cardinality();
		final int and = BitSetUtils.and(left, right).cardinality();	

		final double nom = (double) and;
		final double divisor = (double) (Math.max(card1, card2)); 
		
		final double result = nom / divisor;
		
		return result;
	}
}