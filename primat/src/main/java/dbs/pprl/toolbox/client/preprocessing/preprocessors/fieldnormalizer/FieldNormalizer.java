package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dbs.pprl.toolbox.client.data.attributes.Attribute;
import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.Preprocessor;

public class FieldNormalizer implements Preprocessor {


	private final NormalizeDefinition normalization;
	
	public FieldNormalizer(NormalizeDefinition normalization) {
		this.normalization = normalization;
	}
	
	@Override
	public void apply(List<Record> records, final LinkedList<String> header) throws AttributeParseException {
		for (final Record rec : records){
			this.normalizeRecord(rec);
		}	
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