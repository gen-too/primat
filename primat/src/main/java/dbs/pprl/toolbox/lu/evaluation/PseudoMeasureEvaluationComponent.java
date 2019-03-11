package dbs.pprl.toolbox.lu.evaluation;

import java.util.Set;

import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;
import dbs.pprl.toolbox.utils.Component;

public abstract class PseudoMeasureEvaluationComponent extends Component implements PseudoMeasureEvaluator {

	protected PseudoMeasureEvaluationComponent(){
		super();
	}
	
	protected PseudoMeasureEvaluationComponent(boolean parallelExecution){
		super(parallelExecution);
	}
	
	
	private final void collectPseudoMeasureEvaluationTime(){
		this.metrics.put(TIME_PSEUDO_MEASURE_EVALUATION, this.determineRuntime());
	}

	@Override
	public final void evaluatePseudoQualityMetrics(Set<CandidatePairWithSimilarity> classifiedMatches) {
		this.startTime = System.currentTimeMillis();
		
		this.evaluatePseudoQualityMetricsConcrete(classifiedMatches);
		
		this.endTime = System.currentTimeMillis();
		this.collectPseudoMeasureEvaluationTime();
	}
	
	protected abstract void evaluatePseudoQualityMetricsConcrete(Set<CandidatePairWithSimilarity> classifiedMatches);
}
