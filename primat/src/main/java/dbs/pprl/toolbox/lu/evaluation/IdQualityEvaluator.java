package dbs.pprl.toolbox.lu.evaluation;

import java.math.BigDecimal;
import java.util.Set;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

/**
 * 
 * @author mfranke
 *
 */
public final class IdQualityEvaluator extends QualityEvaluationComponent {
	
	private BigDecimal realMatches;
		
	//TODO: calculate the number of real matches based on the input files
	//TODO: Hashing for equal bloom filter check?
	public IdQualityEvaluator(long realMatches){
		this.realMatches = BigDecimal.valueOf(realMatches);
	}
	
	@Override
	public void evaluateMatchQualityMetrics(Set<CandidatePairWithSimilarity> matches) {
		final BigDecimal classifiedMatches = new BigDecimal(matches.size());
		
		final long trueMatchesLong = this.getTrueMatches(matches);
		this.metrics.put(TRUE_MATCHES, trueMatchesLong);
		
		final BigDecimal trueMatches = BigDecimal.valueOf(trueMatchesLong);
				
		final BigDecimal recall = this.calculateRecall(trueMatches, realMatches);
		final BigDecimal precision = this.calculatePrecision(trueMatches, classifiedMatches);
		final BigDecimal fMeasure = this.calculateFMeasure(recall, precision);
		
		this.metrics.put(RECALL, recall);
		this.metrics.put(PRECISION, precision);
		this.metrics.put(F_MEASURE, fMeasure);
	}

	@Override
	public void evaluateBlockingQualityMetrics(Set<CandidatePair> candidates) {
		final BigDecimal classifiedMatches = new BigDecimal(candidates.size());
		
		final long trueMatchesLong = this.getTrueMatches(candidates);
		
		final BigDecimal trueMatches = BigDecimal.valueOf(trueMatchesLong);
		
		final BigDecimal pairsCompleteness = this.calculateRecall(trueMatches, realMatches);
		final BigDecimal pairsQuality = this.calculatePrecision(trueMatches, classifiedMatches);
		
		this.metrics.put(PAIRS_COMPLETENESS, pairsCompleteness);
		this.metrics.put(PAIRS_QUALITY, pairsQuality);
	}

	@Override
	protected long getTrueMatches(Set<? extends CandidatePair> recordPairs){
		return recordPairs
			.parallelStream()
			.filter(recordPair -> {
				final String leftId = recordPair.getLeftRecord().getId();
				final String rightId = recordPair.getRightRecord().getId();
//				System.out.println(leftId + "-" + rightId + " " + ((CandidatePairWithSimilarity) recordPair).getSimilarity());
				return leftId.equals(rightId);
			})
			.count();
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
}