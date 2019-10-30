package dbs.pprl.toolbox.lu.blocking.standard;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import dbs.pprl.toolbox.lu.blocking.BlockingComponent;
import dbs.pprl.toolbox.lu.blocking.BlockingKey;
import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.evaluation.MetricFormat;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;
import dbs.pprl.toolbox.lu.similarityFunctions.JaccardSimilarity;

/**
 * List of blocking keys is used. Logical OR (at least one common BK).
 * 
 * @author mfranke
 *
 */
public class StandardBlocker extends BlockingComponent{
	
	public StandardBlocker(){
		this(DEFAULT_PARALLEL_EXECUTION);
	}
	
	public StandardBlocker(boolean parallelExecution){
		super(parallelExecution);
	}
	
		
	@Override
	protected Set<CandidatePair> getCandidatePairsConcrete(List<EncodedRecord> recordsPartyA,
			List<EncodedRecord> recordsPartyB) {
		
		final long numberOfRecordsPartyA = recordsPartyA.size();
		final long numberOfRecordsPartyB = recordsPartyB.size();
		final long sizeCartesianProduct = numberOfRecordsPartyA * numberOfRecordsPartyB;
		
//		bla(recordsPartyA, recordsPartyB);
		
		final Set<CandidatePair> candidates = this.parallelExecution ? 
				this.getCandidatePairsParallel(recordsPartyA, recordsPartyB) :
				this.getCandidatePairsSequential(recordsPartyA, recordsPartyB);
				
		final long numberOfCandidates = candidates.size();		
		this.collectReductionRatio(numberOfCandidates, sizeCartesianProduct);
		
		return candidates;
	}
		
	
	private Set<CandidatePair> getCandidatePairsParallel(List<EncodedRecord> recordsPartyA,
			List<EncodedRecord> recordsPartyB){
		System.out.println("Parallel!");
		final Map<BlockingKey<?>, List<EncodedRecord>> blocksPartyA =
				this.getBlocksParallel(recordsPartyA);
		
		final Map<BlockingKey<?>, List<EncodedRecord>> blocksPartyB =
				this.getBlocksParallel(recordsPartyB);
				
		// TODO Maybe do the blocking incrementally, i. e. for each block i do: getBlockPartyA(bk_i) getBlockPartyB(bk_i) buildCandidates(.,.)
		// Result of each iteration is a Set<CandidatePairWithSimilarity> (classified Candidates / Matches)
		// Should save memory, because at a time only on list of records need to be processed
		// For the next Bk the previous blocks can be removed / freed 
		final Set<CandidatePair> candidates = this.buildCandidatePairsParallel(blocksPartyA, blocksPartyB);		
		
		this.collectNumberOfBlocks(blocksPartyA.keySet(), blocksPartyB.keySet());
		this.collectNumberOfCandidates(candidates);
		
		return candidates;
	}
	
	private Map<BlockingKey<?>, List<EncodedRecord>> getBlocksParallel(List<EncodedRecord> records){
		// Keine Unterscheidung nach Parteien mehr, weshalb nach Grouping nicht 
		// mehr feststellbar ist, welche Records verknüpft werden müssen
		// Stream.concat(recordsPartyA.parallelStream(), recordsPartyB.parallelStream())
		return records
			.parallelStream()
//			.stream()
			.flatMap(rec -> {
				final List<? extends BlockingKey<?>> bks = rec.getBlockingKeys();
				final Map<BlockingKey<?>, EncodedRecord> map = new HashMap<BlockingKey<?>, EncodedRecord>(bks.size());
				
				for (final BlockingKey<?> bk : bks){
					map.put(bk, rec);
				}
				
				return map
					.entrySet()
					.parallelStream();
//					.stream();
			})
			.collect(
				Collectors.groupingByConcurrent(
					Entry::getKey, 
					Collectors.mapping(
						Entry::getValue,
						Collectors.toCollection(ArrayList::new)
					)
				)
			); 
	}
	
	// TODO: Should be a global parameter
	public final double threshold = 0.6d;
	
	private Set<CandidatePair> buildCandidatePairsParallel(Map<BlockingKey<?>, List<EncodedRecord>> blocksPartyA, Map<BlockingKey<?>, 
			List<EncodedRecord>> blocksPartyB){
		System.out.println("BUILD CANDIDATES PARALLEL");
		final DescriptiveStatistics blockSizes = new DescriptiveStatistics();

		final Set<CandidatePair>  result = blocksPartyA
			.entrySet()
			.parallelStream()
//			.stream()
			.flatMap(rec -> {
				final BlockingKey<?> blockingKey = rec.getKey();
				
				if (blocksPartyB.containsKey(blockingKey)){
					final List<EncodedRecord> leftRecords = rec.getValue();
					final List<EncodedRecord> rightRecords = blocksPartyB.get(blockingKey);
					
					final List<CandidatePair> candidatesForBlock = 
							new ArrayList<CandidatePair>(leftRecords.size() * rightRecords.size());
					for (final EncodedRecord leftRec : leftRecords){
						for (final EncodedRecord rightRec : rightRecords){			
							
							final BitSet leftBs = leftRec.getBitVectors().get(0);
							final BitSet rightBs = rightRec.getBitVectors().get(0);
							
							JaccardSimilarity jaccard = new JaccardSimilarity();
							final Double sim = jaccard.calculateSimilarity(leftBs, rightBs);
							
//							System.out.println(leftRec.getId() + " - " + rightRec.getId());
//							if (leftRec.getId().equals(rightRec.getId())){
//								System.out.println(sim);
//							}
							
							if (sim.compareTo(threshold) >= 0){
								final CandidatePairWithSimilarity candidatePair 
									= new CandidatePairWithSimilarity(leftRec, rightRec, sim);
								candidatesForBlock.add(candidatePair);
							}
							else{
								// TODO: ID EVAL FOR PAIRS COMPLETENESS
							}
						
							// TODO Seems to work well, since not every pair is materialized so much memory can be saved
							// HERE SHOULD FIRST COME SIM CALC AND THEN CLASSIFICATION PART
							// FOR AUTOMATIC SELECTION HERE THE MIN_T_VALUE IS USED 
							
							// Build Pair only if it has a sufficient similarity score
//								candidatesForBlock.add(candidatePair);	
						
						}	
					}
					blockSizes.addValue(candidatesForBlock.size());
					
					return 
						candidatesForBlock
							.parallelStream();
//							.stream();
				}
				else{
					blockSizes.addValue(0);
					return new ArrayList<CandidatePair>().parallelStream();
				}
				
			})
			.collect(Collectors.toCollection(HashSet::new));
		
		this.collectBlockSizeStatistics(blockSizes);
		
		return result;
	}
	
	
	private Set<CandidatePair> getCandidatePairsSequential(List<EncodedRecord> recordsPartyA,
			List<EncodedRecord> recordsPartyB){
		System.out.println("Sequential!");
		final MultiValuedMap<BlockingKey<?>, EncodedRecord> blocksPartyA =
				this.getBlocksSequential(recordsPartyA);
		
		final MultiValuedMap<BlockingKey<?>, EncodedRecord> blocksPartyB =
				this.getBlocksSequential(recordsPartyB);
				
//		final MultiValuedMap<EncodedRecord, EncodedRecord> cand = this.buildCandidatePairsSequentialNewScheme(blocksPartyA, blocksPartyB);
		
		final Set<CandidatePair> candidates = this.buildCandidatePairsSequential(blocksPartyA, blocksPartyB);
		
		this.collectNumberOfBlocks(blocksPartyA.keySet(), blocksPartyB.keySet());
		this.collectNumberOfCandidates(candidates);
		
//		System.out.println("#Candidates: " + cand.size());
		
		return candidates;
		
	}
	
	private MultiValuedMap<BlockingKey<?>, EncodedRecord> getBlocksSequential(List<EncodedRecord> records){
		final MultiValuedMap<BlockingKey<?>, EncodedRecord> blocksWithRecords
			= new HashSetValuedHashMap<BlockingKey<?>, EncodedRecord>();
		
		for (final EncodedRecord record : records){
			final List<? extends BlockingKey<?>> blockingKeysForRecord = record.getBlockingKeys();
			
			for (final BlockingKey<?> blockingKey : blockingKeysForRecord){
				blocksWithRecords.put(blockingKey, record);
			}
		}
		
		return blocksWithRecords;
	}
	
	
	
	@SuppressWarnings("unused")
	private MultiValuedMap<EncodedRecord, EncodedRecord>  buildCandidatePairsSequentialNewScheme(
			MultiValuedMap<BlockingKey<?>, EncodedRecord> leftSide,
			MultiValuedMap<BlockingKey<?>, EncodedRecord> rightSide){
		
		final DescriptiveStatistics blockSizes = new DescriptiveStatistics();
		
		final MultiValuedMap<EncodedRecord, EncodedRecord> candidates = new HashSetValuedHashMap<>(200_000, 16);		
		
		final Set<BlockingKey<?>> blockingKeysLeftSide = leftSide.keySet();
		
		for (final BlockingKey<?> blockingKey : blockingKeysLeftSide){
			if (rightSide.containsKey(blockingKey)){			
				final Collection<EncodedRecord> leftRecords = leftSide.get(blockingKey);
				final Collection<EncodedRecord> rightRecords = rightSide.get(blockingKey);
				
				for (final EncodedRecord leftRec : leftRecords){
					candidates.putAll(leftRec, rightRecords);		
				}
				
				final int blockSize = leftRecords.size() * rightRecords.size();				
				blockSizes.addValue(blockSize);
			}
			else{
				blockSizes.addValue(0d);
			}
		}
		
		this.collectBlockSizeStatistics(blockSizes);
		
		return candidates;
	}
	
	
	private Set<CandidatePair> buildCandidatePairsSequential(MultiValuedMap<BlockingKey<?>, EncodedRecord> leftSide,
			MultiValuedMap<BlockingKey<?>, EncodedRecord> rightSide){
		
		final DescriptiveStatistics blockSizes = new DescriptiveStatistics();
		
		final Set<CandidatePair> candidates = new HashSet<CandidatePair>();		
		final Set<BlockingKey<?>> blockingKeysLeftSide = leftSide.keySet();
		
		for (final BlockingKey<?> blockingKey : blockingKeysLeftSide){
			if (rightSide.containsKey(blockingKey)){
				final List<CandidatePair> recordsInBlock = this.joinRightRecords(blockingKey, leftSide, rightSide);
				blockSizes.addValue(recordsInBlock.size());
				candidates.addAll(recordsInBlock);
			}
			else{
				blockSizes.addValue(0d);
			}
		}
		
		this.collectBlockSizeStatistics(blockSizes);
		
		return candidates;
	}
	
	private List<CandidatePair> joinRightRecords(BlockingKey<?> blockingKey, 
			MultiValuedMap<BlockingKey<?>, EncodedRecord> leftSide,
			MultiValuedMap<BlockingKey<?>, EncodedRecord> rightSide){	
		
		final Collection<EncodedRecord> leftRecords = leftSide.get(blockingKey);
		final Collection<EncodedRecord> rightRecords = rightSide.get(blockingKey);
		final List<CandidatePair> candidatesForBlock 
			= new ArrayList<CandidatePair>(leftRecords.size() * rightRecords.size());
		
		for (final EncodedRecord leftRec : leftRecords){
			for (final EncodedRecord rightRec : rightRecords){
				final CandidatePair candidatePair 
					= new CandidatePair(leftRec, rightRec);
				
				candidatesForBlock.add(candidatePair);	
			}		
		}
		
		return candidatesForBlock;
	}
	
	private void collectReductionRatio(long numberOfCandidates, long sizeCartesianProduct){
		final BigDecimal candidates = BigDecimal.valueOf(numberOfCandidates);
		final BigDecimal cartesianProduct = BigDecimal.valueOf(sizeCartesianProduct);
		final BigDecimal proportion = MetricFormat.divide(candidates, cartesianProduct);
		final BigDecimal reductionRatio = BigDecimal.ONE.subtract(proportion);
		this.metrics.put(REDUCTION_RATIO, reductionRatio);
	}
	
	
	private void collectNumberOfBlocks(Set<BlockingKey<?>> keysA, Set<BlockingKey<?>> keysB){
		final Set<BlockingKey<?>> keys = new HashSet<BlockingKey<?>>(keysA);
		keys.addAll(keysB);
		this.metrics.put(BLOCKS, keys.size());
	}
	
	private void collectNumberOfCandidates(Set<CandidatePair> candidates){
		this.metrics.put(CANDIDATES, candidates.size());
	}
	
	private void collectBlockSizeStatistics(DescriptiveStatistics blockSizes){
		this.collectAvgBlockSize(blockSizes);
		this.collectMedianBlockSize(blockSizes);
		this.collectLargestBlock(blockSizes);
	}
	
	private void collectAvgBlockSize(DescriptiveStatistics blockSizes){
		final BigDecimal avgBlockSize = MetricFormat.doubleToBigDecimal(blockSizes.getMean());
		this.metrics.put(AVG_BLOCK_SIZE, avgBlockSize);
	}
	
	private void collectMedianBlockSize(DescriptiveStatistics blockSizes){
		final Integer medianBlockSize = MetricFormat.doubleToInteger(blockSizes.getPercentile(50));
		this.metrics.put(MEDIAN_BLOCK_SIZE, medianBlockSize);
	}
	
	private void collectLargestBlock(DescriptiveStatistics blockSizes){
		final Integer largestBlock = MetricFormat.doubleToInteger(blockSizes.getMax());
		this.metrics.put(LARGEST_BLOCK, largestBlock);
	}
	
	public Map<String, Number> getMetrics() {
		return metrics;
	}
}