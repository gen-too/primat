package dbs.pprl.toolbox.lu.matching;

import java.util.List;
import java.util.Set;

import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;
import dbs.pprl.toolbox.utils.Component;

public abstract class MatchingComponent extends Component implements Matcher {

	protected MatchingComponent(){
		this(DEFAULT_PARALLEL_EXECUTION);
	}
	
	protected MatchingComponent(boolean parallelExecution){
		super(parallelExecution);
	}
	
	private final void collectMatchingTime(){
		this.metrics.put(TIME_MATCHING, this.determineRuntime());
	}
	
	@Override
	public final Set<CandidatePairWithSimilarity> match(List<EncodedRecord> recordsPartyA,
			List<EncodedRecord> recordsPartyB) {
		this.startTime = System.currentTimeMillis();
		
		final Set<CandidatePairWithSimilarity> result = this.matchConcrete(recordsPartyA, recordsPartyB);
		
		this.endTime = System.currentTimeMillis();
		this.collectMatchingTime();
		
		return result;
	}

	protected abstract Set<CandidatePairWithSimilarity> matchConcrete(List<EncodedRecord> recordsPartyA,
			List<EncodedRecord> recordsPartyB);
	
}
