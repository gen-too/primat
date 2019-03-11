package dbs.pprl.toolbox.lu.similarityCalculation;

import java.util.BitSet;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.utils.BitSetUtils;

/**
 * 
 * @author mfranke
 *
 */
public final class TanimotoSimilarityCalculator extends BinarySimilarityCalculator {

	public TanimotoSimilarityCalculator(){
		super();
	}

	@Override
	protected double calculateSimilarity(CandidatePair candidatePair) {
		final BitSet leftBitVector = candidatePair.getLeftRecord().getBitVector();
		final BitSet rightBitVector = candidatePair.getRightRecord().getBitVector();

		final int and = BitSetUtils.and(leftBitVector, rightBitVector).cardinality();
		final int or = BitSetUtils.or(leftBitVector, rightBitVector).cardinality();

		final double nom = (double) (and);
		final double divisor = (double) (and + or); 
		
		return nom / divisor;
	}
}