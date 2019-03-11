package dbs.pprl.toolbox.lu.similarityCalculation;

import java.util.BitSet;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.utils.BitSetUtils;

/**
 * 
 * @author mfranke
 *
 */
public class JaccardSimilarityCalculator extends BinarySimilarityCalculator{
	
	public JaccardSimilarityCalculator(){
		super();
	}

	@Override
	protected double calculateSimilarity(CandidatePair candidatePair) {
		return calculateJaccardSimilarity(candidatePair);
	}
	
	public static double calculateJaccardSimilarity(CandidatePair candidatePair) {
		return calculateJaccardSimilarity(candidatePair.getLeftRecord(), candidatePair.getRightRecord());
	}
	
	public static double calculateJaccardSimilarity(EncodedRecord left, EncodedRecord right) {
		final BitSet leftBitVector = left.getBitVector();
		final BitSet rightBitVector = right.getBitVector();
		return calculateJaccardSimilarity(leftBitVector, rightBitVector);
	}
	
	public static double calculateJaccardSimilarity(BitSet leftBitVector, BitSet rightBitVector){
//		return calculateJaccardSimilarityXor(leftBitVector, rightBitVector);
//		/*
		final int card1 = leftBitVector.cardinality();
		final int card2 = rightBitVector.cardinality();
		final int and = BitSetUtils.and(leftBitVector, rightBitVector).cardinality();	

		final double nom = (double) and;
		final double divisor = (double) (card1 + card2 - and); 
		
		return nom / divisor;
//		*/
	}
	
	public static double calculateJaccardSimilarityXor(BitSet leftBitVector, BitSet rightBitVector){
		final int cardLeftUnfolded = 512;
		final int cardRightUnfolded = 512;
		final int cardLeftPlusRightUnfolded = cardLeftUnfolded + cardRightUnfolded;
		final int xor = BitSetUtils.xor(leftBitVector, rightBitVector).cardinality();	

		final double nom = (double) (cardLeftPlusRightUnfolded - xor);
		final double divisor = (double) (cardLeftPlusRightUnfolded + xor); 
		
		return nom / divisor;
	}
}