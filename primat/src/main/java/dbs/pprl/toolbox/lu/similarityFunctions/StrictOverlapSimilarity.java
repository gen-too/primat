package dbs.pprl.toolbox.lu.similarityFunctions;

import java.util.BitSet;

public final class StrictOverlapSimilarity extends OverlapSimilarity{

	@Override
	public double calculateSimilarity(BitSet leftBitVector, BitSet rightBitVector) {
		final double overlapSim = super.calculateSimilarity(leftBitVector, rightBitVector);
		
		if (overlapSim < 1d){
			return 0d;
		}
		else{
			return 1d;
		}
	}	
}