package dbs.pprl.toolbox.data_owner.encoding.bloomfilter;

import java.util.HashSet;
import java.util.Set;

import dbs.pprl.toolbox.data_owner.encoding.bloomfilter.HashUtils.HashingAlgorithm;

public class DoubleHashing extends HashingMethod{

	private HashingAlgorithm hash1;
	private HashingAlgorithm hash2;
	private String seed;
	
	public DoubleHashing(int bfSize) {
		this(bfSize, "", HashingAlgorithm.SHA1, HashingAlgorithm.MD5);
	}
	
	public DoubleHashing(int bfSize, String seed, HashingAlgorithm hash1, HashingAlgorithm hash2) {
		super(bfSize);
		this.seed = seed;
		this.hash1 = hash1;
		this.hash2 = hash2;	
	}
	
	@Override
	public Set<Integer> hash(String element, int hashFunctions) {
		final String input = element + this.salt;
		
		final int hash1 = this.calculateHash1(input);
		final int hash2 = this.calculateHash2(input);		
	
		
		final Set<Integer> positions = new HashSet<>();
		for (int hashNumber = 0; hashNumber < hashFunctions; hashNumber++){
			final int position = Math.abs(hash1 + hashNumber * hash2) % this.bfSize;
			positions.add(position);
		}
		
		return positions;
	}
	
	private int calculateHash1(String element){
		return Math.abs(HashUtils.getHashInt(element + this.seed, this.hash1));
	}
		
	private int calculateHash2(String element){
		return Math.abs(HashUtils.getHashInt(element + this.seed, this.hash2));
	}
}