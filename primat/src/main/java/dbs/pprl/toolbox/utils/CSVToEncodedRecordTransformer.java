package dbs.pprl.toolbox.utils;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVRecord;

import dbs.pprl.toolbox.lu.blocking.BitVectorBlockingKey;
import dbs.pprl.toolbox.lu.blocking.BlockingKey;
import dbs.pprl.toolbox.lu.blocking.StringBlockingKey;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

/**
 * 
 * @author mfranke
 *
 */
public class CSVToEncodedRecordTransformer {

	public static final int ID_FIELD = 0;	// NCVR / GECO
	public static final int BIT_VECTOR_FIELD = 1; //1 | 3             | 1
	public static final int[] BLOCKING_FIELDS = {  }; //- | 1        | 2 ... n
	public static final String[] BLOCKING_KEY_TYPES = { BitVectorBlockingKey.class.getSimpleName() };

	public static final boolean DEFAULT_PARALLEL_EXECUTION = true;
	
	private boolean parallelExecution;
	
	public CSVToEncodedRecordTransformer(){
		this(DEFAULT_PARALLEL_EXECUTION);
	}
	
	public CSVToEncodedRecordTransformer(boolean parallelExecution){
		this.parallelExecution = parallelExecution;
	}
	
	public List<EncodedRecord> transform(List<CSVRecord> records){
		return parallelExecution ? 
			transformParallel(records) :
			transformSequential(records);
	}
	
	private List<EncodedRecord> transformSequential(List<CSVRecord> records){
		final List<EncodedRecord> result = new ArrayList<EncodedRecord>(records.size());
		
		for (final CSVRecord record : records){
			final EncodedRecord encodedRecord = transform(record);
			result.add(encodedRecord);
		}
		
		return result;
	}
	
	private List<EncodedRecord> transformParallel(List<CSVRecord> records){
		return records
//			.stream()
			.parallelStream()
			.map(record -> transform(record))
			.collect(Collectors.toCollection(ArrayList::new));
		
	}	
	
	private EncodedRecord transform(CSVRecord record){
		final String id = record.get(ID_FIELD);
    	final BitSet bitVector = BitSetUtils.fromBase64LittleEndian(record.get(BIT_VECTOR_FIELD));
    	final List<BlockingKey<?>> blockingKeys = 
    			new ArrayList<BlockingKey<?>>(BLOCKING_FIELDS.length);
    	   	
    	for (int bkId = 0; bkId < BLOCKING_FIELDS.length; bkId++) {
    		final String blockingKeyValue = record.get(BLOCKING_FIELDS[bkId]);
    		final String blockingKeyType = BLOCKING_KEY_TYPES[bkId];
    		
    		
    		final boolean isBitVectorBK = blockingKeyType.equals(BitVectorBlockingKey.class.getSimpleName());
    		
    		final BlockingKey<?> bk = isBitVectorBK ? 
    						new BitVectorBlockingKey(bkId, BitSetUtils.fromBase64LittleEndian(blockingKeyValue)) : 
    						new StringBlockingKey(bkId, blockingKeyValue); 
    		

    		blockingKeys.add(bk);
    	}
    	
    	return new EncodedRecord(id, bitVector, blockingKeys);
	}
}