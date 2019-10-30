package dbs.pprl.toolbox.data_owner.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVRecord;

import dbs.pprl.toolbox.data_owner.data.attributes.Attribute;
import dbs.pprl.toolbox.data_owner.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.data_owner.data.attributes.AttributeType;
import dbs.pprl.toolbox.data_owner.data.records.Record;

public class CSVRecordWrapper {

	private final Map<Integer, AttributeType> attrMap;
	
	public CSVRecordWrapper(Map<Integer, AttributeType> attrMap){
		this.attrMap = attrMap;
	}
		
	public List<Record> from(List<CSVRecord> records) throws AttributeParseException{
		final List<Record> result = new ArrayList<Record>(records.size());

		for (final CSVRecord csvRecord : records){
			final Record attRecord = this.from(csvRecord);
			result.add(attRecord);
		}
		
		return result;
	}
	
	private Record from(CSVRecord record) throws AttributeParseException{
		final Record transformedRecord = new Record();
		
		for (final Entry<Integer, AttributeType> mappingEntry : this.attrMap.entrySet()){
			final int column = mappingEntry.getKey();
			final AttributeType attrType = mappingEntry.getValue();
			final String attributeValue = record.get(column);
			final Attribute<?> attr = attrType.constructAttribute();
			attr.setValue(attributeValue);
			transformedRecord.add(attr);
		}	
		
    	return transformedRecord;
	}
}