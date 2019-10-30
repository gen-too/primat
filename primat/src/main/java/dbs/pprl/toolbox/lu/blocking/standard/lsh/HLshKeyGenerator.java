package dbs.pprl.toolbox.lu.blocking.standard.lsh;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import dbs.pprl.toolbox.lu.blocking.BitVectorBlockingKey;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

/**
 * 
 * @author mfranke
 *
 */
public final class HLshKeyGenerator{

	public static final boolean DEFAULT_PARALLEL_EXECUTION = true;

	private static final int SEED = 42;
	
	private Integer keySize;
	private Integer keys;
	private Integer valueRange;
	private Integer[][] keyPositions;
	private boolean parallelExecution;
	
	public HLshKeyGenerator(Integer keySize, Integer keys, Integer valueRange){
		this(keySize, keys, valueRange, DEFAULT_PARALLEL_EXECUTION);
	}
	
	public HLshKeyGenerator(Integer keySize, Integer keys, Integer valueRange, boolean parallelExecution){
		this.keySize = keySize;
		this.keys = keys;
		this.valueRange = valueRange;
		this.keyPositions = null;
		this.parallelExecution = parallelExecution;
	}
	
	
	public void selectRandomKeyPositions(){
		this.selectRandomKeyPositions(null);
	}
	
	public void selectRandomKeyPositions(Integer[] nonFrequentBitPositions){
		final Integer[] randomPositions = this.selectRandomPositions();
		final Integer[][] lshKeyPositions;	
		
		if (nonFrequentBitPositions != null && nonFrequentBitPositions.length == this.valueRange){
			lshKeyPositions = this.mapPositionsToKeys(randomPositions, nonFrequentBitPositions);	
		}
		else{
			lshKeyPositions = this.mapPositionsToKeys(randomPositions);			
		}	
		
		this.keyPositions = lshKeyPositions;
	}	
	
	public void buildHLshKeys(List<EncodedRecord> records){
		if (this.parallelExecution){
			this.buildHLshKeysParallel(records);
		}
		else{
			this.buildHLshKeysSequential(records);
		}
	}
	
	private Integer[] selectRandomPositions(){	
		final Random rnd = new Random(SEED);
		final int positions = this.keys * this.keySize;
	
		if (positions > this.valueRange){// remove for overlapping keys
			throw new RuntimeException("HLSH key size or #keys is to large!");
		}
		
		return rnd
			.ints(0, this.valueRange)
			.distinct()  // remove for overlapping keys
			.limit(positions)
			.boxed()
			.toArray(Integer[]::new);
	}
	
	
	private Integer[][] mapPositionsToKeys(Integer[] positions, Integer[] nonFrequentBitPositions){
		final Integer[][] lshKeyPositions = new Integer[this.keys][this.keySize];
		
		for (int keyIterator = 0; keyIterator < this.keys; keyIterator++){
			final int offset = keyIterator * this.keySize;
			
			for (int posIterator = 0; posIterator < this.keySize; posIterator++){
				lshKeyPositions[keyIterator][posIterator] = nonFrequentBitPositions[positions[posIterator + offset]];	
			}
		}			
		return lshKeyPositions;
	}
	
	private Integer[][] mapPositionsToKeys(Integer[] positions){
		final Integer[][] lshKeyPositions = new Integer[this.keys][this.keySize];
		
		for (int keyIterator = 0; keyIterator < this.keys; keyIterator++){
			final int offset = keyIterator * this.keySize;
			
			for (int posIterator = 0; posIterator < this.keySize; posIterator++){
				lshKeyPositions[keyIterator][posIterator] = positions[posIterator + offset];	
			}
		}				
		return lshKeyPositions;
	}
	
	private void buildHLshKeysParallel(List<EncodedRecord> records){
		records
			.parallelStream()
			.forEach(record -> {
				final List<BitVectorBlockingKey> lshKeys = this.buildHLshKeys(record.getBitVectors().get(0)); 
				record.setBlockingKeys(lshKeys);		
			});
	}
	
	private void buildHLshKeysSequential(List<EncodedRecord> records){
		for (final EncodedRecord record : records){
			final List<BitVectorBlockingKey> lshKeys = this.buildHLshKeys(record.getBitVectors().get(0)); 
			record.setBlockingKeys(lshKeys);	
		}
	}
	
	private List<BitVectorBlockingKey> buildHLshKeys(BitSet record){
		final List<BitVectorBlockingKey> lshKeys = new ArrayList<BitVectorBlockingKey>(this.keys);
		for (int i = 0; i < this.keys; i++){
			final Integer[] positions = this.keyPositions[i];			
			final BitVectorBlockingKey lshKey = this.buildHLshKey(i, positions, record);
			lshKeys.add(lshKey);
		}
		
		return lshKeys;
	}
	
	private BitVectorBlockingKey buildHLshKey(long id, Integer[] positions, BitSet record){
		final BitVectorBlockingKey lshKey = new BitVectorBlockingKey();
		lshKey.setBlockingKeyId(id);
		lshKey.setBlockingKeyValue(this.buildHLshKey(positions, record));
		return lshKey;
	}
	
	private BitSet buildHLshKey(Integer[] positions, BitSet record){
		final BitSet lshKeyValue = new BitSet();
		for (int bitIndex = 0; bitIndex < this.keySize; bitIndex++){
			lshKeyValue.set(bitIndex, record.get(positions[bitIndex]));
		}
		
		return lshKeyValue; 		
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