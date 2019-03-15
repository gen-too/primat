package dbs.pprl.toolbox.client.encoding;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import dbs.pprl.toolbox.client.data.records.PreparedRecord;
import dbs.pprl.toolbox.client.data.records.EncodedRecord;
import dbs.pprl.toolbox.client.encoding.bloomfilter.BloomFilter;
import dbs.pprl.toolbox.client.encoding.bloomfilter.HashFunctionDefinition;
import dbs.pprl.toolbox.client.encoding.bloomfilter.HashingMethod;


/**
 * 
 * @author mfranke
 *
 */
public class FBFBloomFilterEncoder extends BasicBloomFilterEncoder{
	
	
	public FBFBloomFilterEncoder(int bfLength, FeatureExtraction extractor, HashFunctionDefinition hashFunctions, HashingMethod bfHasher) {
		super(bfLength, extractor, hashFunctions, bfHasher);
	}

	@Override
	protected EncodedRecord encode(PreparedRecord preparedRecord) {
		final String id = preparedRecord.getId();
		final EncodedRecord encodedRec = new EncodedRecord(id);

		final Map<Integer, Set<String>> columnToFeatureMap = preparedRecord.getColumnFeatureMap();

		final List<BitSet> fieldBfs = new ArrayList<>(columnToFeatureMap.size());
		
		for (final Entry<Integer, Set<String>> attr : columnToFeatureMap.entrySet()){
			final Integer attribute = attr.getKey();
			final Set<String> features = attr.getValue();
			final int hashFunctionsForAttribute = this.hashFunctions.getHashFunctions(attribute);
			
			final Set<Integer> positions = new HashSet<Integer>();
			
			for (final String feature : features) {
				final Set<Integer> featurePositions = this.bfHasher.hash(feature, hashFunctionsForAttribute);
				positions.addAll(featurePositions);
			}
			
			final BloomFilter bf = new BloomFilter(this.bfLength);
			bf.setPositions(positions);
			fieldBfs.add(bf.getBitVector());
		}
		
		encodedRec.setBitVectors(fieldBfs);
		return encodedRec;
	}
}
