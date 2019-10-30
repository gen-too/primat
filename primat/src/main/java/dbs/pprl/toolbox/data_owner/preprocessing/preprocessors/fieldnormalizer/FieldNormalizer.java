package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dbs.pprl.toolbox.data_owner.data.attributes.Attribute;
import dbs.pprl.toolbox.data_owner.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.data_owner.data.records.Record;
import dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.Preprocessor;

public class FieldNormalizer implements Preprocessor {


	private final NormalizeDefinition normalization;
	
	public FieldNormalizer(NormalizeDefinition normalization) {
		this.normalization = normalization;
	}
	
	public void apply(List<Record> records) throws AttributeParseException {
		for (final Record rec : records){
			this.normalizeRecord(rec);
		}	
	}
	
	@Override
	public void apply(List<Record> records, final LinkedList<String> header) throws AttributeParseException {
		this.apply(records);
	}
	
	private void normalizeRecord(Record record) throws AttributeParseException{
		final Map<Integer, Normalizer> normMap = normalization.getColumnToNormalizerMapping();
		
		for (final Entry<Integer, Normalizer> entry : normMap.entrySet()){
			final int column = entry.getKey();
			final Normalizer normalizer = entry.getValue();
			
			final Attribute<?> attribute = record.getAttribute(column);
			final String attributeValue = attribute.getStringValue();
			final String normalizedValue = normalizer.normalize(attributeValue);
			attribute.setValue(normalizedValue);
		}
	}	
}