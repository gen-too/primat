package dbs.pprl.toolbox.data_owner.encoding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dbs.pprl.toolbox.data_owner.data.attributes.Attribute;
import dbs.pprl.toolbox.data_owner.data.records.PreparedRecord;
import dbs.pprl.toolbox.data_owner.data.records.Record;
import dbs.pprl.toolbox.data_owner.encoding.extractor.ExtractorDefinition;
import dbs.pprl.toolbox.data_owner.encoding.extractor.FeatureExtractor;

public class FeatureExtraction {

	protected final ExtractorDefinition extractorDef;
	
	public FeatureExtraction(ExtractorDefinition extractorDef){
		this.extractorDef = extractorDef;
	}
	
	public List<PreparedRecord> extractFeatures(List<Record> attRecords){
		final List<PreparedRecord> result = new ArrayList<>(attRecords.size());
		
		for (final Record rec : attRecords){
			final PreparedRecord preparedRecord = this.extractFeatures(rec);
			result.add(preparedRecord);
		}
		return result;
	}
		
	public PreparedRecord extractFeatures(Record attRecord){	
		final List<Attribute<?>> attributes = attRecord.getAttributes();	
		
		final PreparedRecord result = new PreparedRecord(attRecord.getId());
			
		for (int col = 0; col < attributes.size(); col++){
			if (extractorDef.hasExtractor(col)){
				final FeatureExtractor featureExtractor = extractorDef.getExtractor(col);
				final Attribute<?> value = attributes.get(col);
				final Set<String> transformedAttr = featureExtractor.extract(value);
				result.add(col, transformedAttr);
			}
			else{
				// don't map / use this attribute !
			}
		}
		return result;
	}
}
