package dbs.pprl.toolbox.lu.postprocessing;

import java.util.Set;

import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;
import dbs.pprl.toolbox.utils.Component;

public abstract class PostprocessorComponent extends Component implements Postprocessor{

	protected PostprocessorComponent(){
		super();
	}
	
	protected PostprocessorComponent(boolean parallelExecution){
		super(parallelExecution);
	}
	
	private final void collectPostprocessingTime(){
		this.metrics.put(TIME_POSTPROCESSING, this.determineRuntime());
	}
	
	@Override
	public final Set<CandidatePairWithSimilarity> clean(Set<CandidatePairWithSimilarity> classifiedMatches) {
		this.startTime = System.currentTimeMillis();
		
		final Set<CandidatePairWithSimilarity> result = this.cleanMatches(classifiedMatches);
		
		this.endTime = System.currentTimeMillis();
		this.collectPostprocessingTime();
		
		return result;
	}
	
	protected abstract Set<CandidatePairWithSimilarity> cleanMatches(Set<CandidatePairWithSimilarity> classifiedMatches);
}