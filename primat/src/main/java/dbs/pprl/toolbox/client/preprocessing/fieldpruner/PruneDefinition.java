package dbs.pprl.toolbox.client.preprocessing.fieldpruner;

import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;


public class PruneDefinition {
		
	private Set<Integer> columnsToPrune;
	
	public PruneDefinition() {
		this.columnsToPrune = new TreeSet<>(Collections.reverseOrder());
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
}