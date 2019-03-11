package dbs.pprl.toolbox.lu.postprocessing;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import dbs.pprl.toolbox.lu.classification.CandidatePairGrouper;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

/**
 * 
 * @author mfranke
 *
 */
public final class MaxBothPostprocessor extends PostprocessorComponent{
	
	public MaxBothPostprocessor(){
		this(DEFAULT_PARALLEL_EXECUTION);
	}
	
	public MaxBothPostprocessor(boolean parallelExecution){
		super(parallelExecution);
	}
	
	
	@Override
	protected Set<CandidatePairWithSimilarity> cleanMatches(Set<CandidatePairWithSimilarity> classifiedMatches) {
		final Set<CandidatePairWithSimilarity> cleanedMatches = this.parallelExecution ? 
				this.cleanParallel(classifiedMatches) :
				this.cleanSequential(classifiedMatches);
				
		return cleanedMatches;
	}
		
	private Set<CandidatePairWithSimilarity> cleanParallel(Set<CandidatePairWithSimilarity> classifiedMatches) {	
		final CandidatePairGrouper grouper = new CandidatePairGrouper(classifiedMatches);
		final Map<EncodedRecord, CandidatePairWithSimilarity> leftNodes = grouper.groupLeftAndGetMaxParallel();
		final Map<EncodedRecord, CandidatePairWithSimilarity> rightNodes = grouper.groupRightAndGetMaxParallel();
		final Set<CandidatePairWithSimilarity> cleanedMatches = this.intersectionParallel(leftNodes, rightNodes);
		return cleanedMatches;
	}

	private Set<CandidatePairWithSimilarity> cleanSequential(Set<CandidatePairWithSimilarity> classifiedMatches) {
		final CandidatePairGrouper grouper = new CandidatePairGrouper(classifiedMatches);
		final Map<String, CandidatePairWithSimilarity> leftNodes = grouper.groupLeftAndGetMaxSequential();
		final Map<String, CandidatePairWithSimilarity> rightNodes = grouper.groupRightAndGetMaxSequential();
		final Set<CandidatePairWithSimilarity> cleanedMatches = this.intersectionSequential(leftNodes, rightNodes);
		return cleanedMatches;
	}
	
		
	private Set<CandidatePairWithSimilarity> intersectionParallel(Map<EncodedRecord, CandidatePairWithSimilarity> leftNodes,
			Map<EncodedRecord, CandidatePairWithSimilarity> rightNodes){
		final Set<CandidatePairWithSimilarity> merged = leftNodes
				.entrySet()
//				.stream()
				.parallelStream()
				.map(Entry::getValue)
				.filter(i -> {
					final CandidatePairWithSimilarity maxCandForRight = rightNodes.get(i.getRightRecord());
					return maxCandForRight.getLeftRecord().equals(i.getLeftRecord());					
				})
				.collect(Collectors.toCollection(HashSet::new));
		System.out.println("Merged " + merged.size());
		return merged;
	}
	
	private Set<CandidatePairWithSimilarity> intersectionSequential(Map<String, CandidatePairWithSimilarity> leftNodes,
			Map<String, CandidatePairWithSimilarity> rightNodes){
		final Collection<CandidatePairWithSimilarity> left = leftNodes.values();
		final Set<CandidatePairWithSimilarity> result = new HashSet<CandidatePairWithSimilarity>();
		
		for (CandidatePairWithSimilarity cand : left){
			final String rightRecordId = cand.getRightRecord().getId();
//			if (rightNodes.containsKey(rightRecordId)){
				final String leftRecordId = rightNodes.get(rightRecordId).getLeftRecord().getId();
				if (cand.getLeftRecord().getId().equals(leftRecordId)){
					result.add(cand);	
				}
			}
//		}			
		System.out.println("Merged " + result.size());
		return result;	
	}
}