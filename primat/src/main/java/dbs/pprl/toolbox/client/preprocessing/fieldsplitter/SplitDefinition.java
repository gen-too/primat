package dbs.pprl.toolbox.client.preprocessing.fieldsplitter;

import java.util.Map;
import java.util.TreeMap;


public class SplitDefinition {
		
	private Map<Integer, Splitter> columnSplitMapping;
	
	public SplitDefinition() {
		this.columnSplitMapping = new TreeMap<>();
	}

	public Splitter setSplitter(int column, Splitter splitter){
		return this.columnSplitMapping.put(column, splitter);
	}
	
	public Splitter getSplitter(int column){
		return this.columnSplitMapping.get(column);
	}
	
	public boolean hasSplitter(int column){
		return this.columnSplitMapping.containsKey(column);
	}
	
	public Map<Integer, Splitter> getColumnToSplitterMapping(){
		return this.columnSplitMapping;
	}
}