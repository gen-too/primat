package dbs.pprl.toolbox.lu.postprocessing;

import java.util.Set;

import dbs.pprl.toolbox.lu.evaluation.MetricCollector;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

/**
 * 
 * @author mfranke
 *
 */
public interface Postprocessor extends MetricCollector{

	public static final String TIME_POSTPROCESSING = "timePostprocessing";
	
	public Set<CandidatePairWithSimilarity> clean(Set<CandidatePairWithSimilarity> classifiedMatches);
}
