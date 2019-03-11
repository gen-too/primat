package dbs.pprl.toolbox.lu.similarityCalculation;

import java.util.BitSet;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.utils.BitSetUtils;

public class OverlapSimilarityCalculator {

	public OverlapSimilarityCalculator(){
		super();
	}

//	@Override
	protected double calculateSimilarity(CandidatePair candidatePair) {
		return calculateOverlapSimilarity(candidatePair);
	}
	
	public static double calculateOverlapSimilarity(CandidatePair candidatePair) {
		return calculateOverlapSimilarity(candidatePair.getLeftRecord(), candidatePair.getRightRecord());
	}
	
	public static double calculateOverlapSimilarity(EncodedRecord left, EncodedRecord right) {
		final BitSet leftBitVector = left.getBitVector();
		final BitSet rightBitVector = right.getBitVector();
		return calculateOverlapSimilarity(leftBitVector, rightBitVector);
	}
	
	public static double calculateOverlapSimilarity(BitSet leftBitVector, BitSet rightBitVector){
		final int card1 = leftBitVector.cardinality();
		final int card2 = rightBitVector.cardinality();
		final int and = BitSetUtils.and(leftBitVector, rightBitVector).cardinality();	

		final double nom = (double) and;
		final double divisor = (double) (Math.min(card1, card2)); 
		
		final double result = nom / divisor;
		
		if (result < 1d){
			return 0d;
		}
		else{
			return 1d;
		}
	}
}