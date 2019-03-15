package dbs.pprl.toolbox.lu.similarityFunctions;

import java.util.BitSet;


import dbs.pprl.toolbox.utils.BitSetUtils;

public class HammingSimilarity extends SimilarityFunction{

	@Override
	public double calculateSim(BitSet left, BitSet right) {		
		final double xor = BitSetUtils.xor(left, right).cardinality();
		
		return 1 - (xor / Math.max(left.length(), right.length()));
	}	
}