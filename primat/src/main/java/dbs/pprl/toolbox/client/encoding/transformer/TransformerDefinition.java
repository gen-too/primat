package dbs.pprl.toolbox.client.encoding.transformer;

import java.util.HashMap;
import java.util.Map;

public class TransformerDefinition {

	private Map<Integer, Transformer> columnTransformerMapping;
	
	public TransformerDefinition() {
		this.columnTransformerMapping = new HashMap<>();
	}

	public Transformer setTransformer(int columnToTransform, Transformer transformer){
		return this.columnTransformerMapping.put(columnToTransform, transformer);
	}
	
	public Transformer getTransformer(int columnToTransform){
		return this.columnTransformerMapping.get(columnToTransform);
	}
	
	public boolean hasTransformer(int columnToTransform){
		return this.columnTransformerMapping.containsKey(columnToTransform);
	}
	
	public Map<Integer, Transformer> getColumnToTransformerMapping(){
		return this.columnTransformerMapping;
	}
}