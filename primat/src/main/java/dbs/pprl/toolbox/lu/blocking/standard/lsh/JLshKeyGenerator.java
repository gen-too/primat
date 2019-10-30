package dbs.pprl.toolbox.lu.blocking.standard.lsh;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dbs.pprl.toolbox.lu.blocking.StringBlockingKey;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

/**
 * 
 * @author mfranke
 *
 */
public final class JLshKeyGenerator{

	public static final boolean DEFAULT_PARALLEL_EXECUTION = true;

	private static final int SEED = 42;
	
	private Integer keySize;
	private Integer keys;
	private Integer valueRange;
	private List<List<List<Integer>>> permutations;
	private boolean parallelExecution;
	
	public JLshKeyGenerator(Integer keySize, Integer keys, Integer valueRange){
		this(keySize, keys, valueRange, DEFAULT_PARALLEL_EXECUTION);
	}
	
	public JLshKeyGenerator(Integer keySize, Integer keys, Integer valueRange, boolean parallelExecution){
		this.keySize = keySize;
		this.keys = keys;
		this.valueRange = valueRange;
		this.permutations = null;
		this.parallelExecution = parallelExecution;
	}
	
		
	public void selectRandomKeyPositions(){
		final List<Integer> positions = new ArrayList<Integer>(this.valueRange);
		for (int i = 0; i < this.valueRange; i++){
			positions.add(i);
		}
		
		final List<List<List<Integer>>> permutations = new ArrayList<>(this.keys);
		for (int keys = 0; keys < this.keys; keys++){
			final List<List<Integer>> permutationsForKey = new ArrayList<>(this.keySize);
			for (int keySize = 0; keySize < this.keySize; keySize++){
				final int seed = SEED + keySize + SEED * keys;
				final Random rnd = new Random(seed);
			
				final List<Integer> permutation = new ArrayList<Integer>(positions);
				Collections.shuffle(permutation, rnd);
				permutationsForKey.add(permutation);
			}
			permutations.add(permutationsForKey);
		}

		this.permutations = permutations;
	}	
	
	public void buildJLshKeys(List<EncodedRecord> records){
		if (this.parallelExecution){
			this.buildJLshKeysParallel(records);
		}
		else{
			this.buildJLshKeysSequential(records);
		}
	}
	
	
	private void buildJLshKeysParallel(List<EncodedRecord> records){
		records
			.parallelStream()
			.forEach(record -> {
				final List<StringBlockingKey> lshKeys = this.buildJLshKeys(record.getBitVectors().get(0)); 
				record.setBlockingKeys(lshKeys);		
			});
	}
	
	private void buildJLshKeysSequential(List<EncodedRecord> records){
		for (final EncodedRecord record : records){
			final List<StringBlockingKey> lshKeys = this.buildJLshKeys(record.getBitVectors().get(0)); 
			record.setBlockingKeys(lshKeys);	
		}
	}
	
	private List<StringBlockingKey> buildJLshKeys(BitSet record){
		final List<StringBlockingKey> lshKeys = new ArrayList<StringBlockingKey>(this.keys);
		for (int i = 0; i < this.keys; i++){
			final List<List<Integer>> permutationForKey = this.permutations.get(i);
			final StringBlockingKey lshKey = this.buildJLshKey(i, permutationForKey, record);
			lshKeys.add(lshKey);
		}
		
		return lshKeys;
	}
	
	private StringBlockingKey buildJLshKey(long id, List<List<Integer>> permutationForKey, BitSet record){
		final StringBlockingKey lshKey = new StringBlockingKey();
		lshKey.setBlockingKeyId(id);
		lshKey.setBlockingKeyValue(this.buildJLshKey(permutationForKey, record));
		return lshKey;
	}
	
	private String buildJLshKey(List<List<Integer>> permutationForKey, BitSet record){
		final StringBuilder JLshKey = new StringBuilder();
		for (int bitIndex = 0; bitIndex < this.keySize; bitIndex++){
			final List<Integer> permutation = permutationForKey.get(bitIndex);
			int resultValue;
			for (int i = 0; i < permutation.size(); i++){
				final int pos = permutation.get(i);
				if (record.get(pos)){
//					resultValue = i;
					resultValue = pos;
					JLshKey.append(resultValue + "_");
					break;
				}
			}
			
		}
		return JLshKey.toString(); 		
	}

	
	public void setKeySize(Integer keySize){
		this.keySize = keySize;
	}
	
	public Integer getKeySize(){
		return this.keySize;
	}
	
	public void setKeys(Integer keys){
		this.keys = keys;
	}
	
	public Integer getKeys(){
		return this.keys;
	}

	public Integer getValueRange() {
		return this.valueRange;
	}

	public void setValueRange(Integer valueRange) {
		this.valueRange = valueRange;
	}
}