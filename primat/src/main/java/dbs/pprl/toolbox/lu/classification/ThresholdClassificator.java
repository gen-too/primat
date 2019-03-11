package dbs.pprl.toolbox.lu.classification;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

/**
 * 
 * @author mfranke
 *
 */
public class ThresholdClassificator extends ClassificatorComponent{

	public static final boolean DEFAULT_PARALLEL_EXECUTION = true;
	
	protected Double threshold;

	
	public ThresholdClassificator(Double threshold){
		this(threshold, DEFAULT_PARALLEL_EXECUTION);
	}
	
	public ThresholdClassificator(Double threshold, boolean parallelExecution){
		super(parallelExecution);
		this.threshold = threshold;
	}
	
	protected Set<CandidatePairWithSimilarity> classifyParallel(Set<CandidatePairWithSimilarity> candidatePairsWithSimilarity) {
		return candidatePairsWithSimilarity
			.parallelStream()
			.filter(candidate -> (
				candidate
					.getSimilarity()
					.compareTo(this.threshold) >= 0)
			)
			.collect(Collectors.toCollection(HashSet::new));
	}
	
	protected Set<CandidatePairWithSimilarity> classifySequential(Set<CandidatePairWithSimilarity> candidatePairsWithSimilarity) {
		final Set<CandidatePairWithSimilarity> result =
				new HashSet<CandidatePairWithSimilarity>();
		
		for (final CandidatePairWithSimilarity candidate : candidatePairsWithSimilarity){
			if (candidate.getSimilarity().compareTo(this.threshold) >= 0){
				result.add(candidate);
			}
		}
		
		return result;
	}
	
	@Override
	protected Set<CandidatePairWithSimilarity> classifyConcrete(Set<CandidatePairWithSimilarity> candidatePairsWithSimilarity) {
		final Set<CandidatePairWithSimilarity> result = this.parallelExecution ?
			this.classifyParallel(candidatePairsWithSimilarity) :
			this.classifySequential(candidatePairsWithSimilarity);
			
		this.metrics.put(CLASSIFIED_MATCHES, result.size());	
		final int prunedCandidates = candidatePairsWithSimilarity.size() - result.size();
		this.metrics.put(CANDIDATES_BELOW_THRESHOLD, prunedCandidates);	
		
		return result;
	}
		
	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}
}