package dbs.pprl.toolbox.client.encoding.bloomfilter;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Random;
import java.util.Set;

import dbs.pprl.toolbox.utils.BitSetUtils;


public class BloomFilter {

	private int size;
	private BitSet bitset;
	//TODO: Create super class and at least two subclasses (random hashing vs. double-hashing-scheme)
//	private boolean useRandomHashing;
		
	public BloomFilter(int size){
		this.size = size;
		this.bitset = new BitSet(size);
	}
	
	@Deprecated
	public void addMappedAttribute(Set<String> element, int hashFunctions){
		for (final String token : element){
			this.addMappedAttribute(token, hashFunctions);
		}
	}
	
	@Deprecated
	public int[] addMappedAttribute(String token, int hashFunctions){
		final int[] positions = new int[hashFunctions];
		for (int hashNumber = 0; hashNumber < hashFunctions; hashNumber++){
			int position = this.hashElement(token, hashNumber);
			this.bitset.set(position);
			positions[hashNumber] = position;
		}
		return positions;
	}
	
	private static Random getRandomNumberGenerator(String token, String key) throws InvalidKeyException, NoSuchAlgorithmException{
		final long hashedToken = HashUtils.getHmacSHA256(token, key);		
		return new Random(hashedToken);
	}
	
	public void addMappedAttributeRandomHashing(Set<String> element, int hashFunctions, String key) throws InvalidKeyException, NoSuchAlgorithmException{
		for (final String token : element){
			this.addMappedAttributeRandomHashing(token, hashFunctions, key);
		}
	}
	
	public int[] addMappedAttributeRandomHashing(String token, int hashFunctions, String key) throws InvalidKeyException, NoSuchAlgorithmException{
		final Random rnd = getRandomNumberGenerator(token, key);
		
		final int[] positions = new int[hashFunctions];
		for (int i = 0; i < hashFunctions; i++){
			final int currentPosition = rnd.nextInt(this.size);
			this.bitset.set(currentPosition);
			positions[i] = currentPosition;
		}
		
		
		return positions;
	}
	
	@Deprecated
	private int hashElement(String element, int hashNumber){
		return (Math.abs(this.hash1(element) + hashNumber * this.hash2(element))) % this.size;
	}
	
	@Deprecated
	private int hash1(String element){
		return Math.abs(HashUtils.getMD5(element));
	}
		
	@Deprecated
	private int hash2(String element){
		return Math.abs(HashUtils.getSHA(element));
	}

	public int getSize() {
		return size;
	}
	
	public BitSet getBitVector(){
		return bitset;
	}
	
	
	public void foldXor(){
		final BitSet firstHalf = this.bitset.get(0, this.size / 2);
		final BitSet secondHalf = this.bitset.get(this.size / 2, this.size);
		this.bitset = BitSetUtils.xor(firstHalf, secondHalf);
		this.size = size / 2;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(bitset);
		builder.append("]");
		return builder.toString();
	}
	
	
}