package dbs.pprl.toolbox.client.preprocessing.fieldmerger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MergeDefinition {
		
	private Map<List<Integer>, Merger> columnMergeMapping;
	
	public MergeDefinition() {
		this.columnMergeMapping = new HashMap<>();
	}

	public Merger setMerger(Integer[] columns, Merger merger){
		return this.columnMergeMapping.put(Arrays.asList(columns), merger);
	}
	
	
	public boolean hasMerger(int columnToNormalize){
		return this.columnMergeMapping.containsKey(columnToNormalize);
	}
	
	public Map<List<Integer>, Merger> getColumnToMergerMapping(){
		return this.columnMergeMapping;
	}
}