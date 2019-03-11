package dbs.pprl.toolbox.lu.classification;

import java.util.Set;

import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;
import dbs.pprl.toolbox.utils.Component;

public abstract class ClassificatorComponent extends Component implements Classificator{
	
	protected ClassificatorComponent(){
		super();
	}
	
	protected ClassificatorComponent(boolean parallelExecution){
		super(parallelExecution);
	}
	
	private final void collectClassificationTime(){
		this.metrics.put(TIME_CLASSIFICATION, this.determineRuntime());
	}

	@Override
	public final Set<CandidatePairWithSimilarity> classify(Set<CandidatePairWithSimilarity> candidatePairsWithSimilarity) {
		this.startTime = System.currentTimeMillis();
		
		final Set<CandidatePairWithSimilarity> result = this.classifyConcrete(candidatePairsWithSimilarity);
		
		this.endTime = System.currentTimeMillis();
		this.collectClassificationTime();
		
		return result;
	}
	
	protected abstract Set<CandidatePairWithSimilarity> classifyConcrete(Set<CandidatePairWithSimilarity> candidatePairsWithSimilarity);	

}