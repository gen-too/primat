package dbs.pprl.toolbox.lu.classification;

import java.util.Set;

import dbs.pprl.toolbox.lu.evaluation.MetricCollector;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

/**
 * 
 * @author mfranke
 *
 */
public interface Classificator extends MetricCollector{

	public static final String TIME_CLASSIFICATION = "timeClassification";

	public static final String CLASSIFIED_MATCHES = "classifiedMatches"; 
	public static final String CANDIDATES_BELOW_THRESHOLD = "candidatesBelowThreshold";
	
	public static final String LEFT_NODES = "leftNodes";
	public static final String LEFT_1_TO_1_MAPPINGS = "left1To1Mappings";
	public static final String LEFT_1_TO_N_MAPPINGS = "left1ToNMappings";
	 
	public static final String RIGHT_NODES = "rightNodes";
	public static final String RIGHT_1_TO_1_MAPPINGS = "right1To1Mappings";
	public static final String RIGHT_1_TO_N_MAPPINGS = "right1ToNMappings";
	
	public static final String MULTI_MATCH_RATIO = "multiMatchRatio";
	
	public Set<CandidatePairWithSimilarity> classify(Set<CandidatePairWithSimilarity> candidatePairsWithSimilarity);
}
