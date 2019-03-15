package dbs.pprl.toolbox.client.encoding.bloomfilter;

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
