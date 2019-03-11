package dbs.pprl.toolbox.lu.blocking;

import java.util.List;
import java.util.Set;

import dbs.pprl.toolbox.lu.evaluation.MetricCollector;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

/**
 * 
 * @author mfranke
 *
 */
public interface Blocker extends MetricCollector{
	
	public static final String TIME_BLOCKING = "timeBlocking";
	
	public static final String CANDIDATES = "candidates";
	public static final String BLOCKS = "blocks";
	public static final String AVG_BLOCK_SIZE = "avgBlockSize";
	public static final String MEDIAN_BLOCK_SIZE = "medianBlockSize";
	public static final String LARGEST_BLOCK = "largestBlock";
	public static final String REDUCTION_RATIO = "reductionRatio";	
	
	public Set<CandidatePair> getCandidatePairs(
			List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB);
}
