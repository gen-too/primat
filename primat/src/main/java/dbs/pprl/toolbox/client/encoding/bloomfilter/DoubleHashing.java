package dbs.pprl.toolbox.client.encoding.bloomfilter;

import java.util.HashSet;
import java.util.Set;

public class DoubleHashing extends HashingMethod{

	public DoubleHashing(int bfSize) {
		super(bfSize);
	}
	
	@Override
	public Set<Integer> hash(String element, int hashFunctions) {
		final String saltedInput = element + this.salt;
		final Set<Integer> positions = new HashSet<>();

		for (int hashNumber = 0; hashNumber < hashFunctions; hashNumber++){
			final int position = this.hashElement(saltedInput, hashNumber);
			positions.add(position);
		}
		
		return positions;
	}


	private int hashElement(String element, int hashNumber){
		return (Math.abs(this.hash1(element) + hashNumber * this.hash2(element))) % this.bfSize;
	}
	
	private int hash1(String element){
		return Math.abs(HashUtils.getMD5(element));
	}
		
	private int hash2(String element){
		return Math.abs(HashUtils.getSHA(element));
	}

}
