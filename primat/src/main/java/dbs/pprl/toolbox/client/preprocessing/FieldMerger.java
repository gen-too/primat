package dbs.pprl.toolbox.client.preprocessing;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;
import dbs.pprl.toolbox.client.preprocessing.fieldmerger.MergeDefinition;
import dbs.pprl.toolbox.client.preprocessing.fieldmerger.Merger;

public class FieldMerger implements Preprocessor{

	
	private final MergeDefinition mergeDef;
	
	
	public FieldMerger(MergeDefinition mergeDef){
		this.mergeDef = mergeDef;
	}	
	
	
	@Override
	public void apply(List<CSVRecordEntry> records, LinkedList<String> header) {		
		for (final CSVRecordEntry rec : records){
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
	
	private void mergeAttributesForRecord(CSVRecordEntry record){
		final List<String> recAttr = record.getAttributes();
	
		final Map<List<Integer>, Merger> fieldToMergerMapping = mergeDef.getColumnToMergerMapping();
		
		for (final Entry<List<Integer>, Merger> mapping : fieldToMergerMapping.entrySet()){
			final List<Integer> indices = mapping.getKey();
			final Merger merger = mapping.getValue();
			
			final String[] attributeValues = this.getAttributeValues(recAttr, indices);
			final String mergedAttribute = merger.merge(attributeValues);
			this.updateAttributeValues(recAttr, indices, mergedAttribute);
		}
	}		
	
	private String[] getAttributeValues(List<String> recAttr, List<Integer> indices){
		final String[] attributeValues = new String[indices.size()];
		
		for (int i = 0; i < indices.size(); i++){
			final int index = indices.get(i);
			attributeValues[i] = recAttr.get(index);
		}
		
		return attributeValues;
	}
	
	private void updateAttributeValues(List<String> recAttr, List<Integer> indices, String mergedAttribute){
		for (int i = indices.size()-1; i >= 0; i--){
			final int index = indices.get(i);
			if (i == 0){
				recAttr.set(index, mergedAttribute);
			}
			else{
				recAttr.remove(index);
			}
		}
	}
}