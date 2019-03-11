package dbs.pprl.toolbox.client.preprocessing;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;
import dbs.pprl.toolbox.client.preprocessing.fieldnormalizer.NormalizeDefinition;
import dbs.pprl.toolbox.client.preprocessing.fieldnormalizer.Normalizer;

public class FieldNormalizer implements Preprocessor {


	private final NormalizeDefinition normalization;
	
	public FieldNormalizer(NormalizeDefinition normalization) {
		this.normalization = normalization;
	}
	
	@Override
	public void apply(List<CSVRecordEntry> records, final LinkedList<String> header) {
		for (final CSVRecordEntry rec : records){
			this.normalizeRecord(rec);
		}	
	}
	
	private void normalizeRecord(CSVRecordEntry record){
		final Map<Integer, Normalizer> normMap = normalization.getColumnToNormalizerMapping();
		
		for (final Entry<Integer, Normalizer> entry : normMap.entrySet()){
			final int columnToMap = entry.getKey();
			final Normalizer normalizer = entry.getValue();
			
			final String attributeValue = record.getAttribute(columnToMap);
			final String normalizedValue = normalizer.normalize(attributeValue);
			record.setAttribute(columnToMap, normalizedValue);
		}
	}	
}