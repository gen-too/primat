package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldpruner;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.Preprocessor;

public class FieldPruner implements Preprocessor {

	private final PruneDefinition pruneDef;
	
	public FieldPruner(PruneDefinition pruneDef){
		this.pruneDef = pruneDef;
	}
	
	@Override
	public void apply(List<Record> records, LinkedList<String> header) {
		for (final Record rec : records){
			this.pruneAttrForRecord(rec);
		}
		this.updateHeader(header);
	}
	
	private void pruneAttrForRecord(Record record){
		final Set<Integer> columnsToPrune = pruneDef.getColumnsToPrune();
		
		for (final Integer col : columnsToPrune){
			record.remove(col);
		}		
	}
		
	private void updateHeader(LinkedList<String> header){
		final Set<Integer> columnsToPrune = pruneDef.getColumnsToPrune();
		
		for (final Integer col : columnsToPrune){
			header.remove(col.intValue());
		}		
	}			
}