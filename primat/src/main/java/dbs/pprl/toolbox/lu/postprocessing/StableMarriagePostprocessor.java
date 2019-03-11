package dbs.pprl.toolbox.lu.postprocessing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Pair;

import dbs.pprl.toolbox.lu.blocking.Side;
import dbs.pprl.toolbox.lu.classification.CandidatePairGrouper;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

public final class StableMarriagePostprocessor extends PostprocessorComponent{
		
	private Map<EncodedRecord, List<CandidatePairWithSimilarity>> leftSide;
	private Map<EncodedRecord, List<CandidatePairWithSimilarity>> rightSide;
	
	public StableMarriagePostprocessor(){
		this(DEFAULT_PARALLEL_EXECUTION);
	}
	
	public StableMarriagePostprocessor(boolean parallelExecution){
		super(parallelExecution);
	}

	@Override
	protected Set<CandidatePairWithSimilarity> cleanMatches(Set<CandidatePairWithSimilarity> classifiedMatches){
		this.groupRecordOnBothSides(classifiedMatches);
		return this.runStableMarriage();
	}
	
	private void groupRecordOnBothSides(Set<CandidatePairWithSimilarity> classifiedMatches){
		final CandidatePairGrouper candidatePairGrouper = new CandidatePairGrouper(classifiedMatches);
		
		if (this.parallelExecution){
			this.leftSide = candidatePairGrouper.groupLeftAndSortBySimilarityParallel();
			this.rightSide = candidatePairGrouper.groupRightAndSortBySimilarityParallel();
		}
		else{
			this.leftSide = candidatePairGrouper.groupLeftAndSortBySimilaritySequential();
			this.rightSide = candidatePairGrouper.groupRightAndSortBySimilaritySequential();
		}
	}
	
	private Set<CandidatePairWithSimilarity> runStableMarriage(){
		final int leftNodeCount = this.leftSide.size();
		final int rightNodeCount = this.rightSide.size();
		
		if (leftNodeCount >= rightNodeCount){
			return runStableMarriageUnequalSets(this.leftSide, this.rightSide, Side.LEFT);
//			return runStableMarriageLeftSideProposes();
		}
		else{
			return runStableMarriageUnequalSets(this.rightSide, this.leftSide, Side.RIGHT);
//			return runStableMarriageRightSideProposes();
		}
	}
	
	private Set<CandidatePairWithSimilarity> runStableMarriageUnequalSets(Map<EncodedRecord, List<CandidatePairWithSimilarity>> smallerSide, Map<EncodedRecord, List<CandidatePairWithSimilarity>> biggerSide, Side smallerSideFlag){
		final List<Entry<EncodedRecord, List<CandidatePairWithSimilarity>>> freeLeftNodesEntry = 
				new LinkedList<Entry<EncodedRecord,List<CandidatePairWithSimilarity>>>(smallerSide.entrySet());

		final List<Pair<EncodedRecord, List<CandidatePairWithSimilarity>>> freeLeftNodes =
			freeLeftNodesEntry
				.stream()
				.map(entry -> 
					new Pair<EncodedRecord, List<CandidatePairWithSimilarity>>(
							entry.getKey(), 
							entry.getValue()
						)
				)
				.collect(Collectors.toCollection(LinkedList::new));
		
		final Map<EncodedRecord, CandidatePairWithSimilarity> rightToLeftMapping = new HashMap<EncodedRecord, CandidatePairWithSimilarity>();			 
		
		while(!freeLeftNodes.isEmpty()){
			final Pair<EncodedRecord, List<CandidatePairWithSimilarity>> currentLeftNode = freeLeftNodes.remove(0);
			final List<CandidatePairWithSimilarity> currentLeftPreferences = currentLeftNode.getValue();
			
			int rejectionCounter = 0;
			for (final CandidatePairWithSimilarity cand : currentLeftPreferences){
				final EncodedRecord currentRightRec = cand.getRecord(Side.other(smallerSideFlag));
								
				// woman not assigned
				if (!rightToLeftMapping.containsKey(currentRightRec)){				
					rightToLeftMapping.put(currentRightRec, cand);				
					break;
				}
				// woman assigned
				else{
					// other man
					final CandidatePairWithSimilarity otherCand = rightToLeftMapping.get(currentRightRec);		
					final EncodedRecord otherLeftRec = otherCand.getRecord(smallerSideFlag);
										
					// womans preferences
					final List<CandidatePairWithSimilarity> currentRightPreferences = biggerSide.get(currentRightRec);
	
					
					final int currentLeftNodeRanking = currentRightPreferences.indexOf(cand);
					final int otherLeftRecRanking = currentRightPreferences.indexOf(otherCand);
									
					// current man better than assigned man
					if (currentLeftNodeRanking < otherLeftRecRanking){
						// replace man
						rightToLeftMapping.put(currentRightRec, cand);
						
						// set man to free
						final List<CandidatePairWithSimilarity> otherLeftRecPreferences = smallerSide.get(otherLeftRec);
						final Pair<EncodedRecord, List<CandidatePairWithSimilarity>> otherLeft =
								new Pair<EncodedRecord, List<CandidatePairWithSimilarity>>(otherLeftRec, otherLeftRecPreferences);
						freeLeftNodes.add(otherLeft);
						
						break;
					}
					else{
						// assigned man is the better man
						rejectionCounter++;
					}	
				}
			}
			if (rejectionCounter == currentLeftPreferences.size()){
				// no partner found
			}
		}

		final Set<CandidatePairWithSimilarity> cleanedMatches = 
				new HashSet<CandidatePairWithSimilarity>(rightToLeftMapping.values());
		
		System.out.println("STABLE RESULT: " + cleanedMatches.size());
		
		return cleanedMatches;
	}
	/*
	private Set<CandidatePairWithSimilarity> runStableMarriageLeftSideProposes(){
		final List<Entry<EncodedRecord, List<CandidatePairWithSimilarity>>> freeLeftNodesEntry = 
				new LinkedList<Entry<EncodedRecord,List<CandidatePairWithSimilarity>>>(leftSide.entrySet());

		final List<Pair<EncodedRecord, List<CandidatePairWithSimilarity>>> freeLeftNodes =
			freeLeftNodesEntry
				.stream()
				.map(entry -> 
					new Pair<EncodedRecord, List<CandidatePairWithSimilarity>>(
							entry.getKey(), 
							entry.getValue()
						)
				)
				.collect(Collectors.toCollection(LinkedList::new));
		
		final Map<EncodedRecord, CandidatePairWithSimilarity> rightToLeftMapping = new HashMap<EncodedRecord, CandidatePairWithSimilarity>();			 
		
		while(!freeLeftNodes.isEmpty()){
			final Pair<EncodedRecord, List<CandidatePairWithSimilarity>> currentLeftNode = freeLeftNodes.remove(0);
			final List<CandidatePairWithSimilarity> currentLeftPreferences = currentLeftNode.getValue();
			
			for (final CandidatePairWithSimilarity cand : currentLeftPreferences){
				final EncodedRecord currentRightRec = cand.getRightRecord();
								
				// woman not assigned
				if (!rightToLeftMapping.containsKey(currentRightRec)){				
					rightToLeftMapping.put(currentRightRec, cand);				
					break;
				}
				// woman assigned
				else{
					// other man
					final CandidatePairWithSimilarity otherCand = rightToLeftMapping.get(currentRightRec);		
					final EncodedRecord otherLeftRec = otherCand.getLeftRecord();
										
					// womans preferences
					final List<CandidatePairWithSimilarity> currentRightPreferences = rightSide.get(currentRightRec);
	
					
					final int currentLeftNodeRanking = currentRightPreferences.indexOf(cand);
					final int otherLeftRecRanking = currentRightPreferences.indexOf(otherCand);
									
					// current man better than assigned man
					if (currentLeftNodeRanking < otherLeftRecRanking){
						// replace man
						rightToLeftMapping.put(currentRightRec, cand);
						
						// set man to free
						final List<CandidatePairWithSimilarity> otherLeftRecPreferences = leftSide.get(otherLeftRec);
						final Pair<EncodedRecord, List<CandidatePairWithSimilarity>> otherLeft =
								new Pair<EncodedRecord, List<CandidatePairWithSimilarity>>(otherLeftRec, otherLeftRecPreferences);
						freeLeftNodes.add(otherLeft);
						
						break;
					}
					else{
						// assigned man is the better man
					}	
				}
			}
		}

		final Set<CandidatePairWithSimilarity> cleanedMatches = 
				new HashSet<CandidatePairWithSimilarity>(rightToLeftMapping.values());
		
		System.out.println("STABLE RESULT: " + cleanedMatches.size());
		
		return cleanedMatches;
	}
	*/
	/*
	private Set<CandidatePairWithSimilarity> runStableMarriageRightSideProposes(){
		final List<Entry<EncodedRecord, List<CandidatePairWithSimilarity>>> freeRightNodesEntry = 
				new LinkedList<Entry<EncodedRecord,List<CandidatePairWithSimilarity>>>(rightSide.entrySet());

		final List<Pair<EncodedRecord, List<CandidatePairWithSimilarity>>> freeRightNodes =
			freeRightNodesEntry
				.stream()
				.map(entry -> 
					new Pair<EncodedRecord, List<CandidatePairWithSimilarity>>(
							entry.getKey(), 
							entry.getValue()
						)
				)
				.collect(Collectors.toCollection(LinkedList::new));
		
		final Map<EncodedRecord, CandidatePairWithSimilarity> leftToRightMapping = new HashMap<EncodedRecord, CandidatePairWithSimilarity>();			 
		
		while(!freeRightNodes.isEmpty()){
			final Pair<EncodedRecord, List<CandidatePairWithSimilarity>> currentRightNode = freeRightNodes.remove(0);
			final List<CandidatePairWithSimilarity> currentRightPreferences = currentRightNode.getValue();
			
			for (final CandidatePairWithSimilarity cand : currentRightPreferences){
				final EncodedRecord currentLeftRec = cand.getLeftRecord();
								
				// man not assigned
				if (!leftToRightMapping.containsKey(currentLeftRec)){				
					leftToRightMapping.put(currentLeftRec, cand);				
					break;
				}
				// man assigned
				else{
					// other woman
					final CandidatePairWithSimilarity otherCand = leftToRightMapping.get(currentLeftRec);		
					final EncodedRecord otherRightRec = otherCand.getRightRecord();
										
					// mans preferences
					final List<CandidatePairWithSimilarity> currentLeftPreferences = leftSide.get(currentLeftRec);
	
					
					final int currentRightNodeRanking = currentLeftPreferences.indexOf(cand);
					final int otherRightRecRanking = currentLeftPreferences.indexOf(otherCand);
									
					// current woman better than assigned woman
					if (currentRightNodeRanking < otherRightRecRanking){
						// replace woman
						leftToRightMapping.put(currentLeftRec, cand);
						
						// set woman to free
						final List<CandidatePairWithSimilarity> otherRightRecPreferences = rightSide.get(otherRightRec);
						final Pair<EncodedRecord, List<CandidatePairWithSimilarity>> otherRight =
								new Pair<EncodedRecord, List<CandidatePairWithSimilarity>>(otherRightRec, otherRightRecPreferences);
						freeRightNodes.add(otherRight);
						
						break;
					}
					else{
						// assigned woman is the better woman
					}	
				}
			}
		}

		final Set<CandidatePairWithSimilarity> cleanedMatches = 
				new HashSet<CandidatePairWithSimilarity>(leftToRightMapping.values());
		
		System.out.println("STABLE RESULT: " + cleanedMatches.size());
		
		return cleanedMatches;
	}
	*/
}
