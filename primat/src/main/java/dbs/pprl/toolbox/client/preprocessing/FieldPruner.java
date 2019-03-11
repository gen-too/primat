package dbs.pprl.toolbox.client.preprocessing;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;
import dbs.pprl.toolbox.client.preprocessing.fieldpruner.PruneDefinition;

public class FieldPruner implements Preprocessor {

	private final PruneDefinition pruneDef;
	
	public FieldPruner(PruneDefinition pruneDef){
		this.pruneDef = pruneDef;
	}
	
	@Override
	public void apply(List<CSVRecordEntry> records, LinkedList<String> header) {
		for (final CSVRecordEntry rec : records){
			this.pruneAttrForRecord(rec);
		}
		this.updateHeader(header);
	}
	
	private void pruneAttrForRecord(CSVRecordEntry record){
		final List<String> recAttr = record.getAttributes();
		final Set<Integer> columnsToPrune = pruneDef.getColumnsToPrune();
		
		for (final Integer col : columnsToPrune){
			recAttr.remove(col.intValue());
		}		
	}
		
	
	private void updateHeader(LinkedList<String> header){
		final Set<Integer> columnsToPrune = pruneDef.getColumnsToPrune();
		
		for (final Integer col : columnsToPrune){
			header.remove(col.intValue());
		}		
	}			
}