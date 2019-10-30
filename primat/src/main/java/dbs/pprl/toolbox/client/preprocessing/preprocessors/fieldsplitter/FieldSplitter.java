package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldsplitter;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import dbs.pprl.toolbox.client.data.attributes.Attribute;
import dbs.pprl.toolbox.client.data.attributes.StringAttribute;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.Preprocessor;

public class FieldSplitter implements Preprocessor{


	private final SplitDefinition splitDef;
	
	public FieldSplitter(SplitDefinition splitDef){
		this.splitDef = splitDef;
	}
	
	@Override
	public void apply(List<Record> records, LinkedList<String> header) {
		for (final Record rec : records){
			this.splitAttrForRecord(rec);
		}
		if (header != null) {
			this.updateHeader(header);
		}
	}
	
	private void splitAttrForRecord(Record record){
		final List<Attribute<?>> recAttr = record.getAttributes();

		final ListIterator<Attribute<?>> it = recAttr.listIterator(recAttr.size());
		
		while(it.hasPrevious()){
			final int index = it.previousIndex();
			final Attribute<?> attribute = it.previous();
			final String value = attribute.getStringValue();
			final Splitter splitter = splitDef.getSplitter(index);
			
			if (splitter != null){
				it.remove();
				final String[] splitRes = splitter.split(value);
				for (final String split : splitRes){
					it.add(new StringAttribute(split));
				}
				for (int i = splitRes.length; i < splitter.parts(); i++){
					it.add(new StringAttribute(""));
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