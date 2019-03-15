package dbs.pprl.toolbox.client.encoding.bloomfilter;

import java.util.BitSet;

import dbs.pprl.toolbox.utils.BitSetUtils;

public class XorFolder implements BloomFilterHardener{

	private final int n;
	
	public XorFolder(int n){
		this.n = n;
	}
	
	@Override
	public void harden(BloomFilter bf) {
		for (int i = 0; i < n; i++){
			this.foldXor(bf);
		}
	}

	public void foldXor(BloomFilter bf){
		final BitSet bitset = bf.getBitVector();
		final int size = bf.getSize();
		
		final BitSet firstHalf = bitset.get(0, size / 2);
		final BitSet secondHalf = bitset.get(size / 2, size);
		
		final BitSet foldedBitset = BitSetUtils.xor(firstHalf, secondHalf);
		bf.setBitVector(foldedBitset);
		bf.setSize(size / 2);
	}

}
