package dbs.pprl.toolbox.lu.evaluation;

import java.util.Set;


import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

/**
 * 
 * @author mfranke
 *
 */
public interface PseudoMeasureEvaluator extends MetricCollector{
	/*
	 * TODO Harmony? Selectivity?
	 * TODO Re-think one-to-one and multi-link ratio, currently only left/right 1-1-links are counted
	 * but if it is an 1-N- or N-1-Link then on one side it is counted as 1-1-link but globally it is no 1-1-link
	 */
	
	public static final String TIME_PSEUDO_MEASURE_EVALUATION = "timePseudoMeasureEvaluation";
	
	public static final String PSEUDO_RECALL_1 = "pseudoRecall1";
	public static final String PSEUDO_RECALL_2 = "pseudoRecall2";
	public static final String PSEUDO_RECALL_3 = "pseudoRecall3";
	
	public static final String PSEUDO_PRECISION_1 = "pseudoPrecision1";
	public static final String PSEUDO_PRECISION_2 = "pseudoPrecision2";
	public static final String PSEUDO_PRECISION_3 = "pseudoPrecision3";
	
	public static final String PSEUDO_F_MEASURE_1 = "pseudoFMeasure1";
	public static final String PSEUDO_F_MEASURE_2 = "pseudoFMeasure2";
	public static final String PSEUDO_F_MEASURE_3 = "pseudoFMeasure3";
			
	public static final String ONE_TO_ONE_LINK_RATIO = "oneToOneLinkRatio";
	public static final String MULTI_LINK_RATIO = "multiLinkRatio";
	
	public static final String LEFT_NODES = "leftNodes";
	public static final String RIGHT_NODES = "rightNodes";
	
	public static final String LEFT_ONE_TO_ONE_LINKS = "leftOneToOneLinks";
	public static final String LEFT_MULTI_LINKS = "leftMultiLinks";
	
	public static final String RIGHT_ONE_TO_ONE_LINKS = "rightOneToOneLinks";
	public static final String RIGHT_MULTI_LINKS = "leftMultiLinks";
	
	public void evaluatePseudoQualityMetrics(Set<CandidatePairWithSimilarity> classifiedMatches);

}