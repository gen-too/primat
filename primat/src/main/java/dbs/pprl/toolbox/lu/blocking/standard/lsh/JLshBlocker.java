package dbs.pprl.toolbox.lu.blocking.standard.lsh;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.blocking.standard.StandardBlocker;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

/**
 * 
 * @author mfranke
 *
 */
public final class JLshBlocker extends StandardBlocker{
	
	public static String TIME_LSH_KEY_GENERATION = "timeLshKeyGeneration";
	public static final boolean DEFAULT_PARALLEL_EXECUTION = true;
	
	private Integer keySize;
	private Integer keys;
	private Integer valueRange;
	
	@Deprecated
	public JLshBlocker(Integer keySize, Integer keys, Integer valueRange){
		this(keySize, keys, valueRange, DEFAULT_PARALLEL_EXECUTION);
	}
	
	@Deprecated
	public JLshBlocker(Integer keySize, Integer keys, Integer valueRange, boolean parallelExecution){
		super(parallelExecution);
		this.keySize = keySize;
		this.keys = keys;
		this.valueRange = valueRange;
	}
	
	public JLshBlocker(JLshBlockerBuilder builder){
		super(DEFAULT_PARALLEL_EXECUTION);
		this.keySize = builder.keySize;
		this.keys = builder.keys;
		this.valueRange = builder.valueRange;
	}
	
	public static class JLshBlockerBuilder{
		public static final Integer DEFAULT_KEY_SIZE = 16;
		public static final Integer DEFAULT_KEYS = 20;
		public static final Integer DEFAULT_VALUE_RANGE = 1024;
		public static final boolean DEFAULT_KEY_RESTRICTION = false;

		private Integer keySize = DEFAULT_KEY_SIZE;
		private Integer keys = DEFAULT_KEYS;
		private Integer valueRange = DEFAULT_VALUE_RANGE;
		
		public JLshBlockerBuilder setKeySize(Integer keySize){
			if (keySize == null) return this;
			
			this.keySize = keySize;
			return this;
		}
		
		public JLshBlockerBuilder setKeys(Integer keys){
			if (keys == null) return this;
			
			this.keys = keys;
			return this;
		}
		
		public JLshBlockerBuilder setValueRange(Integer valueRange){
			if (valueRange == null) return this;
			
			this.valueRange = valueRange;
			return this;
		}
				
		public JLshBlocker build(){
			return new JLshBlocker(this);
		}
	}
	
	@Override
	protected Set<CandidatePair> getCandidatePairsConcrete(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB) {
		final long startTime = System.currentTimeMillis();
		final int actualValueRange = this.valueRange;
				
		final JLshKeyGenerator jlsh = new JLshKeyGenerator(this.keySize, this.keys, actualValueRange);
		
		jlsh.selectRandomKeyPositions();	
		jlsh.buildJLshKeys(recordsPartyA);
		jlsh.buildJLshKeys(recordsPartyB);
		
		this.collectLshKeyGenerationTime(startTime, System.currentTimeMillis());
		
		return super.getCandidatePairsConcrete(recordsPartyA, recordsPartyB);
	}
		
		
	private void collectLshKeyGenerationTime(long startTime, long endTime){
		final BigDecimal time = determineRuntime(startTime, endTime);
		this.metrics.put(TIME_LSH_KEY_GENERATION, time);
	}
}