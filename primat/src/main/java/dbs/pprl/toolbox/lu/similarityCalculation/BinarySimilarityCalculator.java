package dbs.pprl.toolbox.lu.similarityCalculation;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.evaluation.MetricFormat;

/**
 * 
 * @author mfranke
 *
 */
public abstract class BinarySimilarityCalculator extends SimilarityCalculationComponent{
	
	protected BinarySimilarityCalculator(){
		this(DEFAULT_PARALLEL_EXECUTION);
	}
	
	protected BinarySimilarityCalculator(boolean parallelExecution){
		super(parallelExecution);
	}
		
	private void collectSimilarityStatistics(DescriptiveStatistics similarityStat){
		final BigDecimal avgCandidateSimilarity = 
				MetricFormat.doubleToBigDecimal(similarityStat.getMean());
		
		this.metrics.put(AVG_CANDIDATE_SIMILARITY, avgCandidateSimilarity);
		
		final BigDecimal medianCandidateSimilarity = 
				MetricFormat.doubleToBigDecimal(similarityStat.getPercentile(0.5d));
		this.metrics.put(MEDIAN_CANDIDATE_SIMILARITY, medianCandidateSimilarity);
		
		final BigDecimal lowerQuartileCandidateSimilarity = 
				MetricFormat.doubleToBigDecimal(similarityStat.getPercentile(0.25d));
		this.metrics.put(LOWER_QUARTILE_CANDIDATE_SIMILARITY, lowerQuartileCandidateSimilarity);
		
		final BigDecimal upperQuartileCandidateSimilarity = 
				MetricFormat.doubleToBigDecimal(similarityStat.getPercentile(0.75d));
		this.metrics.put(UPPER_QUARTILE_CANDIDATE_SIMILARITY, upperQuartileCandidateSimilarity);
	}
		
	private Set<CandidatePairWithSimilarity> calculateSimilarityParallel(Set<CandidatePair> candidatePairs) {		
		return candidatePairs
			.parallelStream()
//			.stream()
			.map(candidate -> {
				final Double similarity = calculateSimilarity(candidate);
				return new CandidatePairWithSimilarity(candidate, similarity);
			})
			.collect(Collectors.toCollection(HashSet::new));
	}
	
	private Set<CandidatePairWithSimilarity> calculateSimilaritySequential(Set<CandidatePair> candidatePairs) {		
		final Set<CandidatePairWithSimilarity> candidatesWithSimilarity =
				new HashSet<CandidatePairWithSimilarity>(candidatePairs.size());
		
		for (final CandidatePair candidatePair : candidatePairs){
			final Double similarity = this.calculateSimilarity(candidatePair);					
			final CandidatePairWithSimilarity candidatePairWithSimilarity 
				= new CandidatePairWithSimilarity(candidatePair, similarity);
						
			candidatesWithSimilarity.add(candidatePairWithSimilarity);
		}
		
		return candidatesWithSimilarity;
	}
	
	public final Set<CandidatePairWithSimilarity> calculateSimilarityConcrete(Set<CandidatePair> candidatePairs) {		
		
		Set<CandidatePairWithSimilarity> candidatesWithSimilarity =
				this.parallelExecution ?
					this.calculateSimilarityParallel(candidatePairs) :
					this.calculateSimilaritySequential(candidatePairs);
		
		final DescriptiveStatistics similarityStat = new DescriptiveStatistics();
		candidatesWithSimilarity.forEach(candidate -> {
			final Double similarity = candidate.getSimilarity();
			similarityStat.addValue(similarity.doubleValue());
		});
		
		this.collectSimilarityStatistics(similarityStat);
		
		return candidatesWithSimilarity;
	}
	
	
	protected abstract double calculateSimilarity(CandidatePair candidatePair);	
}