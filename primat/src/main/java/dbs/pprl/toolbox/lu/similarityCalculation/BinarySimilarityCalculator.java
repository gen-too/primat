package dbs.pprl.toolbox.lu.similarityCalculation;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.evaluation.MetricFormat;
import dbs.pprl.toolbox.lu.similarityFunctions.SimilarityFunction;

/**
 * 
 * @author mfranke
 *
 */
public class BinarySimilarityCalculator extends SimilarityCalculationComponent{
	
	private SimilarityFunction simFunc;
	
	protected BinarySimilarityCalculator(SimilarityFunction simFunc){
		this(simFunc, DEFAULT_PARALLEL_EXECUTION);
	}
	
	protected BinarySimilarityCalculator(SimilarityFunction simFunc, boolean parallelExecution){
		super(parallelExecution);
		this.simFunc = simFunc;
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
	
	
	protected double calculateSimilarity(CandidatePair candidatePair){
		final List<BitSet> leftBitVectors = candidatePair.getLeftRecord().getBitVectors();
		final List<BitSet> rightBitVectors = candidatePair.getRightRecord().getBitVectors();
		
		final DescriptiveStatistics aggSimilarity = new DescriptiveStatistics();
		
		for (int i = 0; i < leftBitVectors.size(); i++) {
			final BitSet left = leftBitVectors.get(i);
			final BitSet right = rightBitVectors.get(i);
			
			final double similarity = this.simFunc.calculateSimilarity(left, right);
			aggSimilarity.addValue(similarity);
		}
		
		return aggSimilarity.getMean();
	}
}