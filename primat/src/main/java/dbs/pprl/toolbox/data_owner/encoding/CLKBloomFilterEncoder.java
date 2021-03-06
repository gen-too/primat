package dbs.pprl.toolbox.data_owner.encoding;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import dbs.pprl.toolbox.data_owner.data.records.EncodedRecord;
import dbs.pprl.toolbox.data_owner.data.records.PreparedRecord;
import dbs.pprl.toolbox.data_owner.encoding.bloomfilter.BloomFilter;
import dbs.pprl.toolbox.data_owner.encoding.bloomfilter.HashFunctionDefinition;
import dbs.pprl.toolbox.data_owner.encoding.bloomfilter.HashingMethod;

/**
 * 
 * @author mfranke
 *
 */
public class CLKBloomFilterEncoder extends BasicBloomFilterEncoder{
	
	
	public CLKBloomFilterEncoder(int bfLength, FeatureExtraction extractor, HashFunctionDefinition hashFunctions, HashingMethod bfHasher) {
		super(bfLength, extractor, hashFunctions, bfHasher);
	}

	@Override
	protected EncodedRecord encode(PreparedRecord preparedRecord) {
		final String id = preparedRecord.getId();
		final EncodedRecord encodedRec = new EncodedRecord(id);

		final BloomFilter bf = new BloomFilter(this.bfLength);
		final Map<Integer, Set<String>> columnToFeatureMap = preparedRecord.getColumnFeatureMap();
		
		for (final Entry<Integer, Set<String>> attr : columnToFeatureMap.entrySet()){
			final Integer attribute = attr.getKey();
			final Set<String> features = attr.getValue();
			final int hashFunctionsForAttribute = this.hashFunctions.getHashFunctions(attribute);
			
			final Set<Integer> positions = new HashSet<Integer>();
			
			for (final String feature : features) {
				final Set<Integer> featurePositions = this.bfHasher.hash(feature, hashFunctionsForAttribute);
				positions.addAll(featurePositions);
			}
			
			bf.setPositions(positions);
		}
		
		encodedRec.setBitVector(bf.getBitVector());
		return encodedRec;
	}
}
