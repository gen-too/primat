package dbs.pprl.toolbox.data_owner.encoding.extractor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleExtractorDefinition {

	private String name;
	private Set<String> columnNames;
	private Set<Integer> columns;	
	private List<FeatureExtractor> extractors;
	private int numberOfHashFunctions;
	
	public SimpleExtractorDefinition() {
		this("");
	}
	
	public SimpleExtractorDefinition(String name) {
		this.name = name;
		this.columns = new HashSet<>();
		this.extractors = new ArrayList<>();
		this.numberOfHashFunctions = -1;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Integer> getColumns(){
		return this.columns;
	}
	
	public void setExtractors(List<FeatureExtractor> extractors) {
		this.extractors = extractors;
	}
	
	public List<FeatureExtractor> getExtractors(){
		return this.extractors;
	}
	
	public void setColumns(Set<Integer> cols) {
		this.columns = cols;
	}
	
	public void setColumNames(Set<String> colNames) {
		this.columnNames = colNames;
	}
	
	public Set<String> getColumnNames(){
		return this.columnNames;
	}
	
	public void setNumberOfHashFunctions(int hashes) {
		this.numberOfHashFunctions = hashes;
	}
	
	public int getNumberOfHashFunctions() {
		return this.numberOfHashFunctions;
	}
	
	@Override
	public String toString() {
		return this.getName() + " " + this.getColumns();
	}
}