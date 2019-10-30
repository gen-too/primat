package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldpruner;

import java.util.Set;
import java.util.TreeSet;


import java.util.Collections;

public class PruneDefinition {
		
	private Set<Integer> columnsToPrune;
	
	public PruneDefinition(Set<Integer> columnsToPrune) {
		this.columnsToPrune = columnsToPrune;
	}
	
	public PruneDefinition() {
		this(new TreeSet<>(Collections.reverseOrder()));
	}

	public void add(int columnToPrune){
		this.columnsToPrune.add(columnToPrune);
	}

	public boolean contains(int column){
		return this.columnsToPrune.contains(column);
	}
	
	public Set<Integer> getColumnsToPrune(){
		return this.columnsToPrune;
	}
	
	public void setColumnsToPrune(Set<Integer> columnsToPrune) {
		this.columnsToPrune = columnsToPrune;
	}
}