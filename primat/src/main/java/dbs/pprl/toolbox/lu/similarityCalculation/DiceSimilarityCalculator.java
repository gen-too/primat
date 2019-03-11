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
public final class DiceSimilarityCalculator extends BinarySimilarityCalculator {
		
	public DiceSimilarityCalculator(){
		super();
	}

	
	@Override
	protected double calculateSimilarity(CandidatePair candidatePair) {
		return calculateDiceSimilarity(candidatePair);
	}
	
	public static double calculateDiceSimilarity(CandidatePair candidatePair) {
		return calculateDiceSimilarity(candidatePair.getLeftRecord(), candidatePair.getRightRecord());
	}
	
	public static double calculateDiceSimilarity(EncodedRecord left, EncodedRecord right) {
		final BitSet leftBitVector = left.getBitVector();
		final BitSet rightBitVector = right.getBitVector();
		return calculateDiceSimilarity(leftBitVector, rightBitVector);
	}
	
	public static double calculateDiceSimilarity(BitSet leftBitVector, BitSet rightBitVector){
		final int card1 = leftBitVector.cardinality();
		final int card2 = rightBitVector.cardinality();
		final int and = BitSetUtils.and(leftBitVector, rightBitVector).cardinality();	
		
		final double nom = (double) (2 * and);
		final double divisor = (double) (card1 + card2); 
		
		return nom / divisor;
	}
	
	public static double calculateDiceSimilarityXor(BitSet leftBitVector, BitSet rightBitVector){
		final int cardLeftUnfolded = 512;
		final int cardRightUnfolded = 512;
		final int cardLeftPlusRightUnfolded = cardLeftUnfolded + cardRightUnfolded;
		final int xor = BitSetUtils.xor(leftBitVector, rightBitVector).cardinality();	

		final double nom = (double) (cardLeftPlusRightUnfolded - xor);
		final double divisor = (double) (cardLeftPlusRightUnfolded); 
		
		return nom / divisor;
	}
}
