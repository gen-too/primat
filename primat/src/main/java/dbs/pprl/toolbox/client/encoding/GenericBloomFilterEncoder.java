package dbs.pprl.toolbox.client.encoding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dbs.pprl.toolbox.client.data.records.EncodedRecord;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.encoding.bloomfilter.BloomFilter;
import dbs.pprl.toolbox.client.encoding.bloomfilter.HashingMethod;
import dbs.pprl.toolbox.client.encoding.extractor.FeatureExtractor;
import dbs.pprl.toolbox.client.encoding.extractor.SimpleExtractorDefinition;

public final class GenericBloomFilterEncoder implements Encoder {

	private final List<BloomFilterDefinition> bfDefs;
	
	public GenericBloomFilterEncoder(List<BloomFilterDefinition> bfDefs) {
		this.bfDefs = bfDefs;
	}
	
	@Override
	public List<EncodedRecord> encode(List<Record> records) {
		final List<EncodedRecord> encodedRecords = new ArrayList<>(records.size());
		
		for (final Record rec : records) {
			System.out.println("Encode record");
			final EncodedRecord encodedRec = new EncodedRecord(rec.getId());
			final List<BitSet> bitVectors = new ArrayList<>(bfDefs.size());
			
			for (final BloomFilterDefinition bfDef : bfDefs) {
				System.out.println("Encode bloom filter");
				final List<SimpleExtractorDefinition> exDefs = bfDef.getFeatureExtractors();
				final int bfLength = bfDef.getBfLength();
				System.out.println("THE BF LENGTH: " + bfLength);
				final BloomFilter bf = new BloomFilter(bfLength);
				final Set<Integer> positions = new HashSet<>();
				final HashingMethod hashingMethod = bfDef.getHashingMethod();
				
				for (final SimpleExtractorDefinition exDef : exDefs) {
					System.out.println("Encode feat ex");
					final List<FeatureExtractor> featExList = exDef.getExtractors();
					final Set<Integer> columns = exDef.getColumns();
					final int hashFunctions = exDef.getNumberOfHashFunctions();
					
					final LinkedHashSet<String> allFeatures = new LinkedHashSet<>();
					
					for (final Integer col : columns) {
						
						for (final FeatureExtractor featEx : featExList) {
							final LinkedHashSet<String> features = featEx.extract(rec.getAttribute(col));
							allFeatures.addAll(features);
							System.out.println(Arrays.deepToString(allFeatures.toArray()));
						}
						
					}
					
					final Set<Integer> currentPositionSet = hashingMethod.hash(allFeatures, hashFunctions);
					positions.addAll(currentPositionSet);
					System.out.println(positions.size());
				}
				
				bf.setPositions(positions);
				System.out.println(bf);
				bitVectors.add(bf.getBitVector());
			}
			encodedRec.setBitVectors(bitVectors);
			encodedRecords.add(encodedRec);
		}
		
		return encodedRecords;
	}
}