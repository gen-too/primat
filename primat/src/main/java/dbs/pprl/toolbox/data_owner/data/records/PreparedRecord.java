package dbs.pprl.toolbox.data_owner.data.records;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PreparedRecord {

	private final String id;
	private final Map<Integer, Set<String>> columnFeatureMap;
	
	public PreparedRecord(String id){
		this.id = id;
		this.columnFeatureMap = new HashMap<>();
	}
	
	public String getId(){
		return this.id;
	}
	
	public Map<Integer, Set<String>> getColumnFeatureMap(){
		return this.columnFeatureMap;
	}
	
	public Set<String> getFeatures(Integer column){
		return this.columnFeatureMap.get(column);
	}
	
	public void add(Integer column, Set<String> features){
		this.columnFeatureMap.put(column, features);
	}
}
