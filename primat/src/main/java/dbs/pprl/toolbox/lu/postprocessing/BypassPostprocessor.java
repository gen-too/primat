package dbs.pprl.toolbox.lu.postprocessing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

public class BypassPostprocessor implements Postprocessor{

	@Override
	public Map<String, Number> getMetrics() {
		return new HashMap<String, Number>();
	}

	@Override
	public Set<CandidatePairWithSimilarity> clean(Set<CandidatePairWithSimilarity> classifiedMatches) {
		return classifiedMatches;
	}
	
	@Override
	public String toString() {
		return "None";
	}

}
