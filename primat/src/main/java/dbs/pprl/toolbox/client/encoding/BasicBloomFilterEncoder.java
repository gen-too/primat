package dbs.pprl.toolbox.client.encoding;

import java.util.ArrayList;
import java.util.List;

import dbs.pprl.toolbox.client.data.records.EncodedRecord;
import dbs.pprl.toolbox.client.data.records.PreparedRecord;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.encoding.bloomfilter.HashFunctionDefinition;
import dbs.pprl.toolbox.client.encoding.bloomfilter.HashingMethod;

public abstract class BasicBloomFilterEncoder implements Encoder{
	
	protected final int bfLength;
	protected final FeatureExtraction extractor;
	protected final HashFunctionDefinition hashFunctions;
	protected final HashingMethod bfHasher;
	
	public BasicBloomFilterEncoder(int bfLength, FeatureExtraction extractor, HashFunctionDefinition hashFunctions, HashingMethod bfHasher){
		this.bfLength = bfLength;
		this.extractor = extractor;
		this.hashFunctions = hashFunctions;
		this.bfHasher = bfHasher;
	}
	
	@Override
	public List<EncodedRecord> encode(List<Record> records) {
		final List<PreparedRecord> preparedRecords = this.extractor.extractFeatures(records);
		final List<EncodedRecord> result = new ArrayList<EncodedRecord>(preparedRecords.size());
//		final DescriptiveStatistics stat = new DescriptiveStatistics();
	
		for (final PreparedRecord prepRec : preparedRecords){
			final EncodedRecord encodedRec = this.encode(prepRec);
			result.add(encodedRec);
//			stat.addValue(encodedRec.getBitVector().cardinality());
		}
	
//		System.out.println(stat.getMean());
		return result;
	}
	
	protected abstract EncodedRecord encode(PreparedRecord record);
	
	public int getBloomFilterLength() {
		return this.bfLength;
	}
	
	public HashingMethod getHashingMethod() {
		return this.bfHasher;
	}
}
