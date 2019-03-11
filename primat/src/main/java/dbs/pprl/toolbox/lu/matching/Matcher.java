package dbs.pprl.toolbox.lu.matching;

import java.util.List;
import java.util.Set;

import dbs.pprl.toolbox.lu.evaluation.MetricCollector;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

/**
 * 
 * @author mfranke
 *
 */
public interface Matcher extends MetricCollector{
	
	public static final String TIME_MATCHING = "timeMatching";
	
	public static final String RECORDS_PARTY_A = "recordsPartyA";
	public static final String RECORDS_PARTY_B = "recordsPartyB";
	public static final String RESULT_SIZE = "resultSize";
	
	//TODO: Consider other return format: ids, global ids, ...
	public Set<CandidatePairWithSimilarity> match(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB);
	
	// Metrics only computable with gold standard or specific id format, i. e. recall, precision
//	public Map<String, Number> evaluate(Object goldStandard);
}
