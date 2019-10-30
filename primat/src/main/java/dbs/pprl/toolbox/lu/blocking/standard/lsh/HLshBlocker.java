package dbs.pprl.toolbox.lu.blocking.standard.lsh;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.Set;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.math3.fraction.Fraction;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.blocking.standard.StandardBlocker;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

/**
 * 
 * @author mfranke
 *
 */
public final class HLshBlocker extends StandardBlocker{
	
	public static String TIME_LSH_KEY_GENERATION = "timeLshKeyGeneration";
	public static final Fraction PRUNING_PROPORTION_FREQUENT_BITS = Fraction.ONE_QUARTER;
	public static final boolean DEFAULT_PARALLEL_EXECUTION = true;
	
	private Integer keySize;
	private Integer keys;
	private Integer valueRange;
	private boolean keyRestriction;
	
	@Deprecated
	public HLshBlocker(Integer keySize, Integer keys, Integer valueRange, boolean keyRestriction){
		this(keySize, keys, valueRange, keyRestriction, true);
	}
	
	@Deprecated
	public HLshBlocker(Integer keySize, Integer keys, Integer valueRange, boolean keyRestriction, boolean parallelExecution){
		super(parallelExecution);
		this.keySize = keySize;
		this.keys = keys;
		this.valueRange = valueRange;
		this.keyRestriction = keyRestriction;
	}
	
	public HLshBlocker(HLshBlockerBuilder builder){
		super(DEFAULT_PARALLEL_EXECUTION);
		this.keySize = builder.keySize;
		this.keys = builder.keys;
		this.valueRange = builder.valueRange;
		this.keyRestriction = builder.keyRestriction;
	}
	
	public static class HLshBlockerBuilder{
		public static final Integer DEFAULT_KEY_SIZE = 16;
		public static final Integer DEFAULT_KEYS = 20;
		public static final Integer DEFAULT_VALUE_RANGE = 1024;
		public static final boolean DEFAULT_KEY_RESTRICTION = false;

		private Integer keySize = DEFAULT_KEY_SIZE;
		private Integer keys = DEFAULT_KEYS;
		private Integer valueRange = DEFAULT_VALUE_RANGE;
		private boolean keyRestriction = DEFAULT_KEY_RESTRICTION;
		
		public HLshBlockerBuilder setKeySize(Integer keySize){
			if (keySize == null) return this;
			
			this.keySize = keySize;
			return this;
		}
		
		public HLshBlockerBuilder setKeys(Integer keys){
			if (keys == null) return this;
			
			this.keys = keys;
			return this;
		}
		
		public HLshBlockerBuilder setValueRange(Integer valueRange){
			if (valueRange == null) return this;
			
			this.valueRange = valueRange;
			return this;
		}
		
		public HLshBlockerBuilder setKeyRestriction(Boolean keyRestriction){
			if (keyRestriction == null) return this;
			
			this.keyRestriction = keyRestriction;
			return this;
		}
		
		public HLshBlocker build(){
			return new HLshBlocker(this);
		}
	}
	
	@Override
	protected Set<CandidatePair> getCandidatePairsConcrete(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB) {
		final long startTime = System.currentTimeMillis();
		int actualValueRange = this.valueRange;
		Integer[] nonFrequentBitPositions = null;
			
		if (this.keyRestriction){
			nonFrequentBitPositions = this.determineFrequentBitPositions(recordsPartyA, recordsPartyB);
			actualValueRange = nonFrequentBitPositions.length;
		}
		
		final HLshKeyGenerator hlsh = new HLshKeyGenerator(this.keySize, this.keys, actualValueRange);
		
		hlsh.selectRandomKeyPositions(nonFrequentBitPositions);	
		hlsh.buildHLshKeys(recordsPartyA);
		hlsh.buildHLshKeys(recordsPartyB);
		
		this.collectLshKeyGenerationTime(startTime, System.currentTimeMillis());
		
		return super.getCandidatePairsConcrete(recordsPartyA, recordsPartyB);
	}
		
	
	private Integer[] determineFrequentBitPositions(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB){				
		final Map<Integer, Long> bitPositionsMap = 
				this.parallelExecution ? 
					this.getBitPositionFrequencyParallel(recordsPartyA, recordsPartyB) :
					this.getBitPositionFrequencySequential(recordsPartyA, recordsPartyB);
		
		final Integer[] sortedBitPositions = 
				this.parallelExecution ? 
						this.getBitPositionsSortedByFrequencyParallel(bitPositionsMap) :
						this.getBitPositionsSortedByFrequencySequential(bitPositionsMap);
							
		final Integer[] nonFrequentBitPositions =
				this.getNonFrequentBitPositions(sortedBitPositions);
		
		return nonFrequentBitPositions;
	}
	
	private Map<Integer, Long> getBitPositionFrequencyParallel(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB){
		final Map<Integer, Long> posMap  = Stream
			.concat(
				recordsPartyA
					.parallelStream(),
				recordsPartyB
					.parallelStream())
			.flatMapToInt(record -> 
				record
					.getBitVectors().get(0)
					.stream()
					.parallel())
			.boxed()
			.collect(
				Collectors
					.groupingByConcurrent(
						Function.identity(),
						Collectors.counting()
					)
			);
		return posMap;
	}
	
	private Map<Integer, Long> getBitPositionFrequencySequential(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB){
		final Map<Integer, Long> bitpositions = new HashMap<Integer, Long>(this.valueRange);
		
		for (final EncodedRecord rec : recordsPartyA){
			updateBitPositions(bitpositions, rec);
		}
		
		for (final EncodedRecord rec : recordsPartyB){
			updateBitPositions(bitpositions, rec);
		}
		return bitpositions;
	}
	
	private Integer[] getBitPositionsSortedByFrequencyParallel(Map<Integer, Long> bitPositionsMap){
		return bitPositionsMap
			.entrySet()
			.parallelStream()
			.sorted(Map.Entry.comparingByValue())
			.map(Entry::getKey)
			.toArray(Integer[]::new);
	}

	private Integer[] getBitPositionsSortedByFrequencySequential(Map<Integer, Long> bitPositionsMap){
	   	return bitPositionsMap
	   		.entrySet()
			.stream()
			.sorted(Map.Entry.comparingByValue())
			.map(Entry::getKey)
			.toArray(Integer[]::new);
	}
	
	private Integer[] getNonFrequentBitPositions(Integer[] sortedBitPositions){
		final int startIndex = PRUNING_PROPORTION_FREQUENT_BITS.multiply(this.valueRange).intValue();
		final int endIndex = this.valueRange - startIndex; 
		
		return Arrays.copyOfRange(sortedBitPositions, startIndex, endIndex);
	}	
	
	private void updateBitPositions(Map<Integer, Long> bitPositions, EncodedRecord record){
		final BitSet bitVector = record.getBitVectors().get(0);
		for (int i = bitVector.nextSetBit(0); i >= 0; i = bitVector.nextSetBit(i+1)) {		    
		     bitPositions.merge(i, 1L, Long::sum);
		     if (i == Integer.MAX_VALUE) {
		         break; // or (i+1) would overflow
		     }
		 }
	}
	
	private void collectLshKeyGenerationTime(long startTime, long endTime){
		final BigDecimal time = determineRuntime(startTime, endTime);
		this.metrics.put(TIME_LSH_KEY_GENERATION, time);
	}
	
	@Override
	public String toString() {
		return "Hamming LSH";
	}
}