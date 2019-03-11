package dbs.pprl.toolbox.client.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;

public final class RecordEntryTransformer {

	public static List<CSVRecordEntry> from(List<CSVRecord> records, int idCol){
		final List<CSVRecordEntry> result = new ArrayList<>(records.size());
		
		for (final CSVRecord  record : records){		
			result.add(from(record, idCol));
		}
		
		return result;
	}
	
	public static CSVRecordEntry from(CSVRecord record, int idCol){
		final CSVRecordEntry recEntry = new CSVRecordEntry();
		recEntry.setId(record.get(idCol));
		
		final int numberOfAttributes = record.size();
		for (int i = 0; i < numberOfAttributes; i++){
			final String attributeValue = record.get(i);
			recEntry.addAttribute(attributeValue);
		}
		return recEntry;
	}
	
}