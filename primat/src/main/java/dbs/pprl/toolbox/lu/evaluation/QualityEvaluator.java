package dbs.pprl.toolbox.lu.evaluation;

import java.util.Set;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

/**
 * 
 * @author mfranke
 *
 */
public interface QualityEvaluator extends MetricCollector {

//	public static final String TIME_QUALITY_EVALUATION = "timeQualityEvaluation";
	
	public static final String PAIRS_COMPLETENESS = "pairsCompleteness";
	public static final String PAIRS_QUALITY = "pairsQuality";
	
	public static final String RECALL = "recall";
	public static final String PRECISION = "precision";
	public static final String F_MEASURE = "fMeasure";
	
	public static final String TRUE_MATCHES = "trueMatches";
			
	public void evaluateBlockingQualityMetrics(Set<CandidatePair> candidates);	
	
	public void evaluateMatchQualityMetrics(Set<CandidatePairWithSimilarity> matches);
}