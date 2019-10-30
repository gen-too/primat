package dbs.pprl.toolbox.client.encoding.bloomfilter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public abstract class HashingMethod {
	
	protected final int bfSize;
	protected String salt;
	
	protected HashingMethod(int bfSize) {
		this(bfSize, "");
	}
	
	protected HashingMethod(int bfSize, String salt) {
		this.bfSize = bfSize;
		this.salt = salt;
	}
	
	public abstract Set<Integer> hash(String element, int hashFunctions);
	
	public Set<Integer> hash(Collection<String> elements, int hashFunctions){
		final Set<Integer> positions = new HashSet<>();
		for (final String e : elements) {
			System.out.println(e);
			final Set<Integer> positionsForElement = this.hash(e, hashFunctions);
			positions.addAll(positionsForElement);
			System.out.println(positions);
		}
		return positions;
	}
		
	public void setSalt(String salt){
		this.salt = salt;
	}
	
	public String getSalt(){
		return this.salt;
	}
	
	public int getBfSize(){
		return this.bfSize;
	}
}
