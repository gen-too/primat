package dbs.pprl.toolbox.data_owner.encoding.extractor;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines how features are extracted from the attributes.
 * 
 * @author mfranke
 *
 */
public class ExtractorDefinition {

	private Map<Integer, FeatureExtractor> columnTransformerMapping;
	
	public ExtractorDefinition() {
		this.columnTransformerMapping = new HashMap<>();
	}

	public ExtractorDefinition setExtractor(int column, FeatureExtractor featureExtractor){
		this.columnTransformerMapping.put(column, featureExtractor);
		return this;
	}
	
	public FeatureExtractor getExtractor(int column){
		return this.columnTransformerMapping.get(column);
	}
	
	public boolean hasExtractor(int column){
		return this.columnTransformerMapping.containsKey(column);
	}
	
	public Map<Integer, FeatureExtractor> getColumnToExtractorMapping(){
		return this.columnTransformerMapping;
	}
}