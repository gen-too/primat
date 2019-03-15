package dbs.pprl.toolbox.lu.classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import dbs.pprl.toolbox.lu.blocking.Side;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

/**
 * 
 * @author mfranke
 *
 */
public final class CandidatePairGrouper {

	private Set<CandidatePairWithSimilarity> classifiedMatches;
	
	
	public CandidatePairGrouper(Set<CandidatePairWithSimilarity> classifiedMatches){
		this.classifiedMatches = classifiedMatches;
	}
	
	// MAX BOTH
	// ===================================================================================================== //
	public Map<EncodedRecord, CandidatePairWithSimilarity> groupLeftAndGetMaxParallel(){
		return this.groupAndGetMaxParallel(Side.LEFT);
	}
	
	public Map<EncodedRecord, CandidatePairWithSimilarity> groupRightAndGetMaxParallel(){
		return this.groupAndGetMaxParallel(Side.RIGHT);
	}
	
	private Map<EncodedRecord, CandidatePairWithSimilarity> groupAndGetMaxParallel(Side side){
		final Map<EncodedRecord, CandidatePairWithSimilarity> result = this.classifiedMatches
//				.stream()
				.parallelStream()
				.collect(
					Collectors.groupingByConcurrent(
						cand -> cand.getRecord(side),
//						(left ? CandidatePairWithSimilarity::getRecord : CandidatePairWithSimilarity::getRightRecord),
						Collectors.collectingAndThen(
							Collectors.maxBy(Comparator.comparing(CandidatePairWithSimilarity::getSimilarity)),
							Optional::get
						)
					)
				);
		System.out.println("Parallel " + side + ": " + result.size());
		return result;
	}
		
	// ===================================================================================================== //
	
	public Map<String, CandidatePairWithSimilarity> groupLeftAndGetMaxSequential(){
		return this.groupAndGetMaxSequential(Side.LEFT);
	}
	
	public Map<String, CandidatePairWithSimilarity> groupRightAndGetMaxSequential(){
		return this.groupAndGetMaxSequential(Side.RIGHT);
	}
	
	private Map<String, CandidatePairWithSimilarity> groupAndGetMaxSequential(Side side){
		final Map<String, CandidatePairWithSimilarity> nodes = 
				new HashMap<String, CandidatePairWithSimilarity>();
		
		for (final CandidatePairWithSimilarity candidate : this.classifiedMatches){
			final EncodedRecord rec = candidate.getRecord(side);
			final String candidateId = rec.getId();
			if (nodes.containsKey(candidateId)){
				final CandidatePairWithSimilarity nodeMaximum = nodes.get(candidateId);
				if (nodeMaximum.getSimilarity().compareTo(candidate.getSimilarity()) < 0){
					nodes.put(candidateId, candidate);
				}
			}
			else{
				nodes.put(candidateId, candidate);
			}
		}
		
		System.out.println("Sequential " + side + ": " + nodes.size());	
		return nodes;
	}
	
	// HUNGARIAN
	// ===================================================================================================== //
	public Map<EncodedRecord, List<CandidatePairWithSimilarity>> groupLeftParallel(){
		return this.groupParallel(Side.LEFT);
	}
	
	public Map<EncodedRecord, List<CandidatePairWithSimilarity>> groupRightParallel(){
		return this.groupParallel(Side.RIGHT);
	}
	
	private Map<EncodedRecord, List<CandidatePairWithSimilarity>> groupParallel(Side side){
		final Map<EncodedRecord, List<CandidatePairWithSimilarity>> result = this.classifiedMatches
//			.stream()
			.parallelStream()
			.collect(
				Collectors.groupingBy(
					cand -> cand.getRecord(side),
//					(left ? CandidatePairWithSimilarity::getLeftRecord : CandidatePairWithSimilarity::getRightRecord),
					Collectors.toCollection(ArrayList::new)
				)
			);
		
		return result;
	}
	
	public Map<EncodedRecord, List<EncodedRecord>> groupLeftAndMapRightRecordsParallel(){
		return this.groupAndMapRecordsParallel(Side.LEFT);
	}
	
	public Map<EncodedRecord, List<EncodedRecord>> groupRightAndMapLeftRecordsParallel(){
		return this.groupAndMapRecordsParallel(Side.RIGHT);
	}
	
	private Map<EncodedRecord, List<EncodedRecord>> groupAndMapRecordsParallel(Side side){
		final Map<EncodedRecord, List<EncodedRecord>> result = this.classifiedMatches
//			.stream()
			.parallelStream()
			.collect(
				Collectors.groupingBy(
					cand -> cand.getRecord(side),
//					(left ? CandidatePairWithSimilarity::getLeftRecord : CandidatePairWithSimilarity::getRightRecord),
					Collectors.mapping(
						cand -> cand.getRecord(Side.other(side)),
//						(left ? CandidatePairWithSimilarity::getRightRecord : CandidatePairWithSimilarity::getLeftRecord),
						Collectors.toCollection(ArrayList::new)
					)
				)
			);
		
		return result;
	}
		
	
	// ===================================================================================================== //
	public Map<EncodedRecord, List<CandidatePairWithSimilarity>> groupLeftSequential(){
		return this.groupSequential(Side.LEFT);
	}
	
	public Map<EncodedRecord, List<CandidatePairWithSimilarity>> groupRightSequential(){
		return this.groupSequential(Side.RIGHT);
	}
	
	private Map<EncodedRecord, List<CandidatePairWithSimilarity>> groupSequential(Side side){
		final HashMap<EncodedRecord, List<CandidatePairWithSimilarity>> nodes 
				= new HashMap<EncodedRecord, List<CandidatePairWithSimilarity>>();
		
		for (final CandidatePairWithSimilarity candidate : this.classifiedMatches){
			final EncodedRecord rec = candidate.getRecord(side);
			
			if (nodes.containsKey(rec)){
				final List<CandidatePairWithSimilarity> candidates = nodes.get(rec);
				candidates.add(candidate);
			}
			else{
				final List<CandidatePairWithSimilarity> candidates = new ArrayList<CandidatePairWithSimilarity>();
				candidates.add(candidate);
				nodes.put(rec, candidates);
			}
		}
		
		return nodes;
	}
	
	public Map<EncodedRecord, List<EncodedRecord>> groupLeftAndMapRightRecordsSequential(){
		return this.groupAndMapRecordsSequential(Side.LEFT);
	}
	
	public Map<EncodedRecord, List<EncodedRecord>> groupRightAndMapLeftRecordsSequential(){
		return this.groupAndMapRecordsSequential(Side.RIGHT);
	}
	
	private Map<EncodedRecord, List<EncodedRecord>> groupAndMapRecordsSequential(Side side){
		final HashMap<EncodedRecord, List<EncodedRecord>> nodes 
				= new HashMap<EncodedRecord, List<EncodedRecord>>();
		
		for (final CandidatePairWithSimilarity candidate : this.classifiedMatches){
			final EncodedRecord rec = candidate.getRecord(side);
			final EncodedRecord mappedCandidate = candidate.getRecord(Side.other(side));
			
			if (nodes.containsKey(rec)){
				final List<EncodedRecord> candidates = nodes.get(rec);
				candidates.add(mappedCandidate);
			}
			else{
				final List<EncodedRecord> candidates = new ArrayList<EncodedRecord>();
				candidates.add(mappedCandidate);
				nodes.put(rec, candidates);
			}
		}
		
		return nodes;
	}
	
	
	
	// STABLE MARRIAGE
	// ===================================================================================================== //
	public Map<EncodedRecord, List<CandidatePairWithSimilarity>> groupLeftAndSortBySimilarityParallel(){
		return this.groupAndSortBySimilarityParallel(Side.LEFT);
	}
	
	public Map<EncodedRecord, List<CandidatePairWithSimilarity>> groupRightAndSortBySimilarityParallel(){
		return this.groupAndSortBySimilarityParallel(Side.RIGHT);
	}
	
	private Map<EncodedRecord, List<CandidatePairWithSimilarity>> groupAndSortBySimilarityParallel(Side side){
		final Map<EncodedRecord, List<CandidatePairWithSimilarity>> nodes = this.classifiedMatches
//			.stream()
			.parallelStream()
			.collect(
				Collectors.groupingBy(
					cand -> cand.getRecord(side),
//					(left ? CandidatePairWithSimilarity::getLeftRecord : CandidatePairWithSimilarity::getRightRecord),
					Collectors.collectingAndThen(
						Collectors.toCollection(ArrayList::new), 
						assignedCandidates -> {
							assignedCandidates.sort(CandidatePairWithSimilarity.BY_SIMILARITY);
							return assignedCandidates; 
						}	
					)
				)
			);
		System.out.println("Parallel " + side + ": " + nodes.size());
		return nodes;
	}
	// ===================================================================================================== //
	public Map<EncodedRecord, List<CandidatePairWithSimilarity>> groupLeftAndSortBySimilaritySequential(){
		return this.groupAndSortBySimilaritySequential(Side.LEFT);
	}
	
	public Map<EncodedRecord, List<CandidatePairWithSimilarity>> groupRightAndSortBySimilaritySequential(){
		return this.groupAndSortBySimilaritySequential(Side.RIGHT);
	}
	
	private Map<EncodedRecord, List<CandidatePairWithSimilarity>> groupAndSortBySimilaritySequential(Side side){
		final Map<EncodedRecord, List<CandidatePairWithSimilarity>> nodes = this.groupSequential(side);
		
		for (final Entry<EncodedRecord, List<CandidatePairWithSimilarity>> entry : nodes.entrySet()){
			Collections.sort(entry.getValue(), CandidatePairWithSimilarity.BY_SIMILARITY);
		}
		
		System.out.println("Sequential " + side + ": " + nodes.size());
		return nodes;
	}
	
	
	
	// PSEUDO MEASURES
	// ===================================================================================================== //
	public Map<EncodedRecord, Long> groupLeftAndCountParallel(){
		return this.groupAndCountParallel(Side.LEFT);
	}
	
	public Map<EncodedRecord, Long> groupRightAndCountParallel(){
		return this.groupAndCountParallel(Side.RIGHT);
	}
	
	private Map<EncodedRecord, Long> groupAndCountParallel(Side side){
		final Map<EncodedRecord, Long> nodes = this.classifiedMatches
//			.stream()
			.parallelStream()
			.collect(
				Collectors.groupingByConcurrent(
					cand -> cand.getRecord(side),
//					(left ? CandidatePairWithSimilarity::getLeftRecord : CandidatePairWithSimilarity::getRightRecord),
					Collectors.counting()
				)
			);
		
		System.out.println("Parallel " + side + ": " + nodes.size());
		return nodes;
	}
	
	
	public Map<Boolean, Long> groupLeftAndCountLinksByTypeParallel(){
		final Map<EncodedRecord, Long> left = this.groupLeftAndCountParallel();
		return this.countLinksByTypeParallel(left);
	}
	
	public Map<Boolean, Long> groupLeftAndCountLinksByTypeParallel(Map<EncodedRecord, Long> left){
		return this.countLinksByTypeParallel(left);
	}
	
	public Map<Boolean, Long> groupRightAndCountLinksByTypeParallel(){
		final Map<EncodedRecord, Long> right = this.groupRightAndCountParallel();
		return this.countLinksByTypeParallel(right);
	}
	
	public Map<Boolean, Long> groupRightAndCountLinksByTypeParallel(Map<EncodedRecord, Long> right){
		return this.countLinksByTypeParallel(right);
	}
	
	private Map<Boolean, Long> countLinksByTypeParallel(Map<EncodedRecord, Long> nodes){
		final Map<Boolean, Long> countMap = nodes
			.entrySet()
			.parallelStream()
			.map(rec -> {
				final boolean isOneToOneLink = (rec.getValue() == 1);
				return isOneToOneLink;
			})
			.collect(
				Collectors.groupingByConcurrent(
					Function.identity(), 
					Collectors.counting()
				)
			);
		return countMap;
	}
	
	
	// ===================================================================================================== //
	public Map<EncodedRecord, Long> groupLeftAndCountSequential(){
		return this.groupAndCountSequential(Side.LEFT);
	}
	
	public Map<EncodedRecord, Long> groupRightAndCountSequential(){
		return this.groupAndCountSequential(Side.RIGHT);
	}
	
	private Map<EncodedRecord, Long> groupAndCountSequential(Side side){
		final Map<EncodedRecord, Long> nodes = 
				new HashMap<EncodedRecord, Long>();
		
		for (final CandidatePairWithSimilarity candidate : this.classifiedMatches){
			final EncodedRecord rec = candidate.getRecord(side);
			nodes.merge(rec, 1L, Long::sum);
		}
		
		System.out.println("Sequential " + side + ": " + nodes.size());
		return nodes;
	}
	
	
	public Map<Boolean, Long> groupLeftAndCountLinksByTypeSequential(){
		final Map<EncodedRecord, Long> left = this.groupLeftAndCountSequential();
		return this.countLinksByTypeParallel(left);
	}
	
	public Map<Boolean, Long> groupLeftAndCountLinksByTypeSequential(Map<EncodedRecord, Long> left){
		return this.countLinksByTypeParallel(left);
	}
	
	public Map<Boolean, Long> groupRightAndCountLinksByTypeSequential(){
		final Map<EncodedRecord, Long> right = this.groupRightAndCountSequential();
		return this.countLinksByTypeSequential(right);
	}
	
	public Map<Boolean, Long> groupRightAndCountLinksByTypeSequential(Map<EncodedRecord, Long> right){
		return this.countLinksByTypeSequential(right);
	}
	
	private Map<Boolean, Long> countLinksByTypeSequential(Map<EncodedRecord, Long> nodes){
		final Set<Entry<EncodedRecord, Long>> entrySet = nodes.entrySet();
		final Map<Boolean, Long> countMap = new HashMap<Boolean, Long>();
		
		for(final Entry<EncodedRecord, Long> entry : entrySet){
			final boolean isOneToOneLink = (entry.getValue() == 1);
			countMap.merge(Boolean.valueOf(isOneToOneLink), 1L, Long::sum);
		}
		
		return countMap;
	}		
}