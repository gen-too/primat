package dbs.pprl.toolbox.lu.postprocessing;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.math3.util.Pair;
import dbs.pprl.toolbox.lu.classification.CandidatePairGrouper;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

/**
 * 
 * @author mfranke
 *
 */
public final class HungarianPostprocessor extends PostprocessorComponent{

	private BidiMap<EncodedRecord, Integer> leftRecordPositionAssignment;
	private BidiMap<EncodedRecord, Integer> rightRecordPositionAssignment;
		
	public HungarianPostprocessor(){
		this(DEFAULT_PARALLEL_EXECUTION);
	}
	
	public HungarianPostprocessor(boolean parallelExecution){
		super(parallelExecution);
	}

	@Override
	protected Set<CandidatePairWithSimilarity> cleanMatches(Set<CandidatePairWithSimilarity> classifiedMatches) {
		final Set<CandidatePairWithSimilarity> cleanMatches = this.cleanHungarian(classifiedMatches);
		System.out.println("HUNG RESULT: " + cleanMatches.size());
		
		return cleanMatches;
	}
		
	
	private Set<CandidatePairWithSimilarity> cleanHungarian(Set<CandidatePairWithSimilarity> classifiedMatches){
		
		final MultiValuedMap<Integer, CandidatePairWithSimilarity> clusters = 
				this.getConnectedComponents(classifiedMatches);
	
		System.out.println("Number of clusters: " + clusters.keySet().size());
				
//		int sum = clusterAssign.asMap().entrySet().stream()
//			.collect(Collectors.summingInt(i -> i.getValue().size()));
//		System.out.println(sum);
		
		final Set<CandidatePairWithSimilarity> cleanedMatches = new HashSet<CandidatePairWithSimilarity>();
				
		clusters
			.asMap()
			.entrySet()
//			.parallelStream()
			.forEach(cluster -> {
				final Set<CandidatePairWithSimilarity> candidatesInCluster = (Set<CandidatePairWithSimilarity>) cluster.getValue();
				if (candidatesInCluster.size() == 1){
					cleanedMatches.addAll(candidatesInCluster);
				}
				else{
					final double[][] clusterCostMatrix = this.constructCostMatrix(candidatesInCluster);
					final HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(clusterCostMatrix);
					final int[] clusterAssignmentMatrix = hungarianAlgorithm.execute();				
					this.applyAssignmentMatrix(candidatesInCluster, clusterAssignmentMatrix);
					cleanedMatches.addAll(candidatesInCluster);
				}
		});		
		
		return cleanedMatches;
	}
	
	private Map<Boolean, Set<EncodedRecord>> initVisitedMap(int leftNodeSize, int rightNodeSize){
		final Map<Boolean, Set<EncodedRecord>> visited = new HashMap<Boolean, Set<EncodedRecord>>(2);
		
		final Set<EncodedRecord> visitedLeft = new HashSet<EncodedRecord>(leftNodeSize);
		final Set<EncodedRecord> visitedRight = new HashSet<EncodedRecord>(rightNodeSize);

		visited.put(true, visitedLeft);
		visited.put(false, visitedRight);
		
		return visited;
	}

	private Map<Boolean, Map<EncodedRecord, Integer>> initClusterAssignmentMap(Map<EncodedRecord, List<CandidatePairWithSimilarity>> leftNodes, 
			int rightNodeSize){
		final Map<Boolean, Map<EncodedRecord, Integer>> clusterAssign = new HashMap<Boolean, Map<EncodedRecord, Integer>>(2);
		
		final Map<EncodedRecord, Integer> clusterAssignLeft = new HashMap<EncodedRecord, Integer>(leftNodes.size());
		final Map<EncodedRecord, Integer> clusterAssignRight = new HashMap<EncodedRecord, Integer>(rightNodeSize);

		int id = 0;
		for (final EncodedRecord key : leftNodes.keySet()){
			clusterAssignLeft.put(key, id);
			id++;
		}
		
		clusterAssign.put(true, clusterAssignLeft);
		clusterAssign.put(false, clusterAssignRight);	
		
		return clusterAssign;
	}
		
	private ArrayDeque<Boolean> initSideStack(int leftNodeSize){
		final ArrayDeque<Boolean> sideStack = new ArrayDeque<Boolean>(leftNodeSize);
		
		for (int i = 0; i < leftNodeSize; i++){
			sideStack.push(true);
		}
		
		return sideStack;
	}
	
	private ArrayDeque<Pair<EncodedRecord, List<CandidatePairWithSimilarity>>> initBaseStack(
			Map<EncodedRecord, List<CandidatePairWithSimilarity>> leftNodes){
		return leftNodes
		.entrySet()
		.stream()
		.map(i -> {
			final Pair<EncodedRecord, List<CandidatePairWithSimilarity>> pair = 
					new Pair<EncodedRecord, List<CandidatePairWithSimilarity>>(i.getKey(), i.getValue());
			return pair;
		})
		.collect(Collectors.toCollection(ArrayDeque::new));
	}
	
	private MultiValuedMap<Integer, CandidatePairWithSimilarity> getConnectedComponents(Set<CandidatePairWithSimilarity> classifiedMatches){
		final CandidatePairGrouper grouper = new CandidatePairGrouper(classifiedMatches);
		final Map<EncodedRecord, List<CandidatePairWithSimilarity>> leftNodes = grouper.groupLeftParallel();
		final Map<EncodedRecord, List<CandidatePairWithSimilarity>> rightNodes = grouper.groupRightParallel();
		
		final int leftNodeSize = leftNodes.size();
		final int rightNodeSize = rightNodes.size();
		
		final Map<Boolean, Map<EncodedRecord, Integer>> clusterAssign = this.initClusterAssignmentMap(leftNodes, rightNodeSize);
		final Map<Boolean, Set<EncodedRecord>> visited = this.initVisitedMap(leftNodeSize, rightNodeSize);
		
		final MultiValuedMap<Integer, CandidatePairWithSimilarity> clusters = 
//				new ArrayListValuedHashMap<>();
				new HashSetValuedHashMap<>();
				
		final ArrayDeque<Pair<EncodedRecord, List<CandidatePairWithSimilarity>>> baseStack = 
				this.initBaseStack(leftNodes);
		
		final ArrayDeque<Pair<EncodedRecord, List<CandidatePairWithSimilarity>>> deepStack = 
				new ArrayDeque<Pair<EncodedRecord, List<CandidatePairWithSimilarity>>>();
	
		final ArrayDeque<Boolean> sideStack = this.initSideStack(leftNodeSize);
		
		int clusterId = -1;		
		while (!baseStack.isEmpty()){
			final boolean fromDeepStack = !deepStack.isEmpty();
			
			// if deepStack is empty get from normal stack
			final Pair<EncodedRecord, List<CandidatePairWithSimilarity>> leftRec = 
					fromDeepStack ? 
						deepStack.pop() : 
						baseStack.pop();
			
			final EncodedRecord left = leftRec.getKey();
			final boolean side = sideStack.pop();
		
			if (!visited.get(side).contains(left)){
				// Mark as visited
				visited.get(side).add(left);
				
				// Forward current cluster id
				if (fromDeepStack){
					clusterAssign.get(side).put(left, clusterId);
				}
				// Get previous cluster id 
				else{
					if (!side) throw new RuntimeException();
					final Integer previousClusterId  = clusterAssign.get(side).get(left);
					clusterId = previousClusterId;
				}
								
				// Get assigned records on other side
				final List<CandidatePairWithSimilarity> rightRecs = leftRec.getValue();			
				final boolean otherSide = !side;
				
				if (side){
					clusters.putAll(clusterId, rightRecs);
				}
				
				for (final CandidatePairWithSimilarity right : rightRecs){				
					final EncodedRecord rightRec = otherSide ? right.getLeftRecord() : right.getRightRecord();					
					final List<CandidatePairWithSimilarity> lis = otherSide ? leftNodes.get(rightRec) : rightNodes.get(rightRec);	
					final Pair<EncodedRecord, List<CandidatePairWithSimilarity>> w = new Pair<>(rightRec, lis);
					
					deepStack.push(w);
					sideStack.push(otherSide);							
				}
			}
			else{
				// not already visited
			}
		}
						
		return clusters;
	}
		

	private double[][] constructCostMatrix(Set<CandidatePairWithSimilarity> classifiedMatches){
		final CandidatePairGrouper grouper = new CandidatePairGrouper(classifiedMatches);
		final Map<EncodedRecord, List<CandidatePairWithSimilarity>> leftSide = grouper.groupLeftParallel();
		final Map<EncodedRecord, List<CandidatePairWithSimilarity>> rightSide = grouper.groupRightParallel();

		this.leftRecordPositionAssignment = new DualHashBidiMap<EncodedRecord, Integer>();
		this.rightRecordPositionAssignment = new DualHashBidiMap<EncodedRecord, Integer>();
		
		final Set<Entry<EncodedRecord, List<CandidatePairWithSimilarity>>> leftRecs = leftSide.entrySet();
		final Set<Entry<EncodedRecord, List<CandidatePairWithSimilarity>>> rightRecs = rightSide.entrySet();

		final int numberOfLeftRecs = leftRecs.size();
		final int numberOfRightRecs = rightRecs.size();
		
		final double[][] costMatrix = new double[numberOfLeftRecs][numberOfRightRecs];
		
		int leftIndex = 0;
		int rightIndex = 0;
		
		for (final Entry<EncodedRecord, List<CandidatePairWithSimilarity>> leftRec : leftRecs){
			leftRecordPositionAssignment.put(leftRec.getKey(), leftIndex);
			
			final List<CandidatePairWithSimilarity> pairs = leftRec.getValue();
			for (final CandidatePairWithSimilarity pair : pairs){
				final EncodedRecord rightRecord = pair.getRightRecord();
				final double sim = pair.getSimilarity().doubleValue();
				
				if (!rightRecordPositionAssignment.containsKey(rightRecord)){
					rightRecordPositionAssignment.put(pair.getRightRecord(), rightIndex);
					costMatrix[leftIndex][rightIndex] = sim;				
					rightIndex++;
				}
				else{
					final int rightIndexPosition = rightRecordPositionAssignment.get(rightRecord);
					costMatrix[leftIndex][rightIndexPosition] = sim;	
				}		
			}
			leftIndex++;
		}
		
		for (int i = 0; i < numberOfLeftRecs; i++){
			for (int j = 0; j < numberOfRightRecs; j++){
				final double sim = costMatrix[i][j];
				if (sim == 0d){
					costMatrix[i][j] = 1d;
				}
				else{
					costMatrix[i][j] = 1d - sim;
				}
			}
		}
		
		return costMatrix;
	}
	
	private Set<CandidatePairWithSimilarity> applyAssignmentMatrix(Set<CandidatePairWithSimilarity> classifiedMatches, int[] assignmentMatrix){
		final Set<CandidatePairWithSimilarity> cleanCands = new HashSet<CandidatePairWithSimilarity>();
		
		for (int leftIndex = 0; leftIndex < assignmentMatrix.length; leftIndex++){
			final int rightIndex = assignmentMatrix[leftIndex];
			if (rightIndex != -1){
				final EncodedRecord leftRec = this.leftRecordPositionAssignment.getKey(leftIndex);
				final EncodedRecord rightRec = this.rightRecordPositionAssignment.getKey(rightIndex);
				final CandidatePairWithSimilarity cand = new CandidatePairWithSimilarity(leftRec, rightRec, 0d);
				cleanCands.add(cand);
			}
			else{
				// No assignment for this left node
			}
		}
		
		classifiedMatches.retainAll(cleanCands);
		
		return classifiedMatches;
	}
	
	@Override
	public String toString() {
		return "Hungarian Algorithm (Maximum Weight Matching)";
	}
}