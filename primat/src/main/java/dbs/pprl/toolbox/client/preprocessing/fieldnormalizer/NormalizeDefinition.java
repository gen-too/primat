package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;

import java.util.HashMap;
import java.util.Map;

public class NormalizeDefinition {
		
	private Map<Integer, Normalizer> columnNormalizerMapping;
	
	public NormalizeDefinition() {
		this.columnNormalizerMapping = new HashMap<>();
	}

	public Normalizer setNormalizer(int columnToNormalize, Normalizer normalizer){
		return this.columnNormalizerMapping.put(columnToNormalize, normalizer);
	}
	
	public Normalizer getNormalizer(int columnToNormalize){
		return this.columnNormalizerMapping.get(columnToNormalize);
	}
	
	public Map<Integer, Normalizer> getColumnToNormalizerMapping(){
		return this.columnNormalizerMapping;
	}
}