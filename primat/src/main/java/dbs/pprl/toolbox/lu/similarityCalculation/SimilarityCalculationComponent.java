package dbs.pprl.toolbox.lu.similarityCalculation;

import java.util.Set;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.utils.Component;

public abstract class SimilarityCalculationComponent extends Component implements SimilarityCalculator{
	
	protected SimilarityCalculationComponent(){
		super();
	}
	
	protected SimilarityCalculationComponent(boolean parallelExecution){
		super(parallelExecution);
	}
	
	private final void collectSimilarityCalculationTime(){
		this.metrics.put(TIME_SIMILARITY_CALCULATION, this.determineRuntime());
	}

	@Override
	public final Set<CandidatePairWithSimilarity> calculateSimilarity(Set<CandidatePair> candidatePairs) {
		this.startTime = System.currentTimeMillis();
		
		final Set<CandidatePairWithSimilarity> result = this.calculateSimilarityConcrete(candidatePairs);
		
		this.endTime = System.currentTimeMillis();
		this.collectSimilarityCalculationTime();
		
		return result;
	}
	
	protected abstract Set<CandidatePairWithSimilarity> calculateSimilarityConcrete(Set<CandidatePair> candidatePairs);
}