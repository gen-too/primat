package dbs.pprl.toolbox.client.encoding;

import java.util.List;

import dbs.pprl.toolbox.client.encoding.bloomfilter.HashingMethod;
import dbs.pprl.toolbox.client.encoding.extractor.SimpleExtractorDefinition;

public class BloomFilterDefinition {

	private int bfLength;
	private List<SimpleExtractorDefinition> featureExtractors;
	private HashingMethod hashingMethod;
	
	public BloomFilterDefinition() {}

	public int getBfLength() {
		return bfLength;
	}

	public void setBfLength(int bfLength) {
		this.bfLength = bfLength;
	}
	
	public List<SimpleExtractorDefinition> getFeatureExtractors() {
		return this.featureExtractors;
	}
	
	public void setFeatureExtractors(List<SimpleExtractorDefinition> featEx) {
		this.featureExtractors = featEx;
	}

	public HashingMethod getHashingMethod() {
		return hashingMethod;
	}

	public void setHashingMethod(HashingMethod hashingMethod) {
		this.hashingMethod = hashingMethod;
	}
}