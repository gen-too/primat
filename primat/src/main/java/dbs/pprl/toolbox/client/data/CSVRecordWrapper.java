package dbs.pprl.toolbox.client.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVRecord;

import dbs.pprl.toolbox.client.data.attributes.Attribute;
import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.attributes.AttributeType;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.encoding.ColumnAttributTypeMapping;

public class CSVRecordWrapper {

	private final ColumnAttributTypeMapping attrMap;
	
	public CSVRecordWrapper(ColumnAttributTypeMapping attrMap){
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
	
	public Record from(CSVRecord record) throws AttributeParseException{
		final Record transformedRecord = new Record();
		final Map<Integer, AttributeType> mapping = this.attrMap.getMapping();
		
		for (final Entry<Integer, AttributeType> mappingEntry : mapping.entrySet()){
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
