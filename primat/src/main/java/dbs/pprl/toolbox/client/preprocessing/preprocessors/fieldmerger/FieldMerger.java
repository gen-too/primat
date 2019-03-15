package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldmerger;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import dbs.pprl.toolbox.client.data.attributes.Attribute;
import dbs.pprl.toolbox.client.data.attributes.StringAttribute;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.Preprocessor;

public class FieldMerger implements Preprocessor{

	
	private final MergeDefinition mergeDef;
	
	
	public FieldMerger(MergeDefinition mergeDef){
		this.mergeDef = mergeDef;
	}	
	
	
	@Override
	public void apply(List<Record> records, LinkedList<String> header) {		
		for (final Record rec : records){
			this.mergeAttributesForRecord(rec);
		}
		this.updateHeader(header);
	}

	// 3,4 -> DotMerger und 4,3 -> DotMerger?
	private void updateHeader(LinkedList<String> header){
		final Map<List<Integer>, Merger> fieldToMergerMapping = this.mergeDef.getColumnToMergerMapping();
		
		for (final List<Integer> columns : fieldToMergerMapping.keySet()){
						
			final Integer firstCol = columns.get(0);
			final String firstVal = header.get(firstCol);
			final StringJoiner newValue = new StringJoiner("_");
			newValue.add(firstVal);
			
			for (int i = 1; i < columns.size(); i++) {
				final String oldVal = header.remove(columns.get(i).intValue());
				newValue.add(oldVal);
			}	
			
			header.set(firstCol, newValue.toString());
		}
	}
	
	private void mergeAttributesForRecord(Record record){
		final Map<List<Integer>, Merger> fieldToMergerMapping = mergeDef.getColumnToMergerMapping();
		
		for (final Entry<List<Integer>, Merger> mapping : fieldToMergerMapping.entrySet()){
			final List<Integer> indices = mapping.getKey();
			final Merger merger = mapping.getValue();
			
			final String[] attributeValues = this.getAttributeValues(record, indices);
			final String mergedAttributeValue = merger.merge(attributeValues);
			
			for (int i = 0; i < indices.size() - 1; i++) {
				record.remove(i);
			}
			
			final int lastPosition = indices.get(indices.size() - 1);
			final Attribute<?> mergedAttribute = new StringAttribute(mergedAttributeValue);
			record.add(lastPosition, mergedAttribute);
		}
	}		
	
	private String[] getAttributeValues(Record record, List<Integer> indices){
		final String[] attributeValues = new String[indices.size()];
		
		for (int i = 0; i < indices.size(); i++){
			final int index = indices.get(i);
			attributeValues[i] = record.getAttribute(index).getStringValue();
		}
		
		return attributeValues;
	}
}