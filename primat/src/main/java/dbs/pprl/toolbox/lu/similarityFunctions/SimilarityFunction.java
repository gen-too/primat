package dbs.pprl.toolbox.lu.similarityFunctions;

import java.util.BitSet;

import dbs.pprl.toolbox.utils.BitSetUtils;

public abstract class SimilarityFunction {
	
	protected BitSet bitMask;
	
	public SimilarityFunction(){
		this.bitMask = null;
	}
	
	public SimilarityFunction(BitSet bitMask){
		this.bitMask = bitMask;
	}
		
	private BitSet applyBitMask(BitSet bitVector){
		if (this.bitMask == null){
			return bitVector;
		}
		else {
			return BitSetUtils.andNot(bitVector, this.bitMask);
		}
	}

	public double calculateSimilarity(BitSet leftBitVector, BitSet rightBitVector){
		final BitSet left = this.applyBitMask(leftBitVector);
		final BitSet right = this.applyBitMask(rightBitVector);
		return calculateSim(left, right);
	}
	
	protected abstract double calculateSim(BitSet leftBitVector, BitSet rightBitVector);
}
