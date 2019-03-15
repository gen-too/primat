package dbs.pprl.toolbox.client.encoding.bloomfilter;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines how many hash functions for attribute features.
 * 
 * @author mfranke
 *
 */
public class HashFunctionDefinition {

	private Map<Integer, Integer> columnToNumberOfHashFunctions;
	
	public HashFunctionDefinition() {
		this.columnToNumberOfHashFunctions = new HashMap<>();
	}

	public HashFunctionDefinition setHashFunctions(int column, int hashFunctions){
		this.columnToNumberOfHashFunctions.put(column, hashFunctions);
		return this;
	}
	
	public int getHashFunctions(int column){
		return this.columnToNumberOfHashFunctions.get(column);
	}
	
	public boolean hasExtractor(int column){
		return this.columnToNumberOfHashFunctions.containsKey(column);
	}
	
	public Map<Integer, Integer> getColumnToHashFunctionsMapping(){
		return this.columnToNumberOfHashFunctions;
	}
}