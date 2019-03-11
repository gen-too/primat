package dbs.pprl.toolbox.lu.blocking;

import java.util.List;
import java.util.Set;

import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.utils.Component;

public abstract class BlockingComponent extends Component implements Blocker{

	protected BlockingComponent(){
		super();
	}
	
	protected BlockingComponent(boolean parallelExecution){
		super(parallelExecution);
	}
	
	private final void collectBlockingTime(){
		this.metrics.put(TIME_BLOCKING, this.determineRuntime());
	}
	
	@Override
	public final Set<CandidatePair> getCandidatePairs(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB) {
		this.startTime = System.currentTimeMillis();
		
		final Set<CandidatePair> result = this.getCandidatePairsConcrete(recordsPartyA, recordsPartyB);
		
		this.endTime = System.currentTimeMillis();
		this.collectBlockingTime();
		
		return result;
	}
			
	protected abstract Set<CandidatePair> getCandidatePairsConcrete(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB);	
}