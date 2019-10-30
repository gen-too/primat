package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleNormalizeDefinition {

	private String name;
	
	private Set<String> columnNames;
	
	private Set<Integer> columns;
	
	private List<Normalizer> normalizer;
	
	
	public SimpleNormalizeDefinition() {
		this("");
	}
	
	public SimpleNormalizeDefinition(String name) {
		this.name = name;
		this.columns = new HashSet<>();
		this.normalizer = new ArrayList<>();
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
	
	public List<Normalizer> getNormalizer(){
		return this.normalizer;
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
	
	public void setNormalizer(List<Normalizer> norm) {
		this.normalizer = norm;
	}
	
	@Override
	public String toString() {
		return this.getName() + " " + this.getColumns();
	}
}
