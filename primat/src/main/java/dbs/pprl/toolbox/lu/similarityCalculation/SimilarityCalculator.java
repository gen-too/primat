package dbs.pprl.toolbox.lu.similarityCalculation;

import java.util.Set;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.evaluation.MetricCollector;

/**
 * 
 * @author mfranke
 *
 */
public interface SimilarityCalculator extends MetricCollector{

	public static final String TIME_SIMILARITY_CALCULATION = "timeSimilarityCalculation";
	
	public static final String AVG_CANDIDATE_SIMILARITY = "avgCandidateSimilarity";
	public static final String MEDIAN_CANDIDATE_SIMILARITY = "medianCandidateSimilarity";
	public static final String UPPER_QUARTILE_CANDIDATE_SIMILARITY = "upperQuartileCandidateSimilarity";
	public static final String LOWER_QUARTILE_CANDIDATE_SIMILARITY = "lowerQuartileCandidateSimilarity";

	public Set<CandidatePairWithSimilarity> calculateSimilarity(Set<CandidatePair> candidatePairs);
}
