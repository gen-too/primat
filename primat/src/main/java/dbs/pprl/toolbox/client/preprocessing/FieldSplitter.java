package dbs.pprl.toolbox.client.preprocessing;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;
import dbs.pprl.toolbox.client.preprocessing.fieldsplitter.SplitDefinition;
import dbs.pprl.toolbox.client.preprocessing.fieldsplitter.Splitter;

public class FieldSplitter implements Preprocessor{


	private final SplitDefinition splitDef;
	
	public FieldSplitter(SplitDefinition splitDef){
		this.splitDef = splitDef;
	}
	
	@Override
	public void apply(List<CSVRecordEntry> records, LinkedList<String> header) {
		for (final CSVRecordEntry rec : records){
			this.splitAttrForRecord(rec);
		}
		this.updateHeader(header);
	}
	
	private void splitAttrForRecord(CSVRecordEntry record){
		final List<String> recAttr = record.getAttributes();

		final ListIterator<String> it = recAttr.listIterator(recAttr.size());
		
		while(it.hasPrevious()){
			final int index = it.previousIndex();
			final String value = it.previous();
			final Splitter splitter = splitDef.getSplitter(index);
			
			if (splitter != null){
				it.remove();
				final String[] splitRes = splitter.split(value);
				for (final String split : splitRes){
					it.add(split);
				}
				for (int i = splitRes.length; i < splitter.parts(); i++){
					it.add("");
				}
				for (int i = 0; i < splitter.parts(); i++){
					it.previous();
				}
			}	
		}
	}
	
	private void updateHeader(LinkedList<String> header){	
		
		final ListIterator<String> it = header.listIterator();
		
		int indexOffset = 0;
		
		while(it.hasNext()) {		
			final int index = it.nextIndex();
			final String value = it.next();
			final Splitter splitter = this.splitDef.getSplitter(index - indexOffset);
			
			if (splitter != null){
				it.set(value + "_0");
				for (int i = 1; i < splitter.parts(); i++){
					it.add(value + "_" + i);
					indexOffset++;
				}
			}
		}
	}	
}