package dbs.pprl.toolbox.lu.classification;



/**
 * 
 * @author mfranke
 *
 */
public final class AdaptiveThresholdClassificator{ //extends ThresholdClassificator{
	/*
	 * TODO Further work needed here. How to find a stopping criteria?
	 */
	/*
	public static final BigDecimal HIGHEST_SIMILARITY = BigDecimal.ONE.setScale(2);
	public static final BigDecimal LOWEST_SIMILARITY = new BigDecimal("0.60");
	public static final BigDecimal STEP_LENGTH = new BigDecimal("0.05");
	
	public AdaptiveThresholdClassificator(){
		this(LOWEST_SIMILARITY);
	}
	
	public AdaptiveThresholdClassificator(BigDecimal thresholdLowerBound) {
		super(thresholdLowerBound);
	}

	public Set<CandidatePairWithSimilarity> classifyConcrete(Set<CandidatePairWithSimilarity> candidatePairsWithSimilarity) {
		Set<CandidatePairWithSimilarity> result = this.parallelExecution ?
			this.classifyParallel(candidatePairsWithSimilarity) :
			this.classifySequential(candidatePairsWithSimilarity);
	
		final Map<BigDecimal, Map<String, Integer>> leftLinkStatusMap = collectLeftLinkStatus(result);	
		final Map<String, Integer> leftCompleteMap = getCompleteMap(leftLinkStatusMap);
		final Map<Integer, Integer> leftCountMap = getCountMap(leftCompleteMap);
		
		System.out.println(leftCountMap.size());
		
		final Map<BigDecimal, Map<String, Integer>> rightLinkStatusMap = collectRightLinkStatus(result);	
		final Map<String, Integer> rightCompleteMap = getCompleteMap(rightLinkStatusMap);
		final Map<Integer, Integer> rightCountMap = getCountMap(rightCompleteMap);
		
		System.out.println(rightCountMap.size());		
		
		final Map<Boolean, Integer> leftSimpleCountMap = getSimpleCountMap(leftCompleteMap);
		System.out.println(leftSimpleCountMap);
		
		final Map<Boolean, Integer> rightSimpleCountMap = getSimpleCountMap(rightCompleteMap);
		System.out.println(rightSimpleCountMap);	
			
		this.metrics.put(CLASSIFIED_MATCHES, result.size());	
		final int prunedCandidates = candidatePairsWithSimilarity.size() - result.size();
		this.metrics.put(CANDIDATES_BELOW_THRESHOLD, prunedCandidates);	
				
		return result;
	}
	
	private static Map<BigDecimal, Map<String, Integer>> collectLeftLinkStatus(Set<CandidatePairWithSimilarity> classifiedMatches){
		return collectLinkStatus(classifiedMatches, true);
	}
	
	private static Map<BigDecimal, Map<String, Integer>> collectRightLinkStatus(Set<CandidatePairWithSimilarity> classifiedMatches){
		return collectLinkStatus(classifiedMatches, false);
	}
	
	// group by rounded similarity
	private static Map<BigDecimal, Map<String, Integer>> collectLinkStatus(Set<CandidatePairWithSimilarity> classifiedMatches, boolean left){
		final Map<BigDecimal, Map<String, Integer>> side = 
				new HashMap<BigDecimal, Map<String, Integer>>(9);
		
		initializeLinkStatusMap(side);
			
		for (final CandidatePairWithSimilarity candidate : classifiedMatches){
			final EncodedRecord rec = left ? candidate.getLeftRecord() : candidate.getRightRecord();
			final String candidateId = rec.getId();
			final BigDecimal similarity = candidate.getSimilarity();
			final BigDecimal roundedSim = roundSimilarity(similarity);
			final Map<String, Integer> mapForSim = side.get(roundedSim);
			
			if (mapForSim != null){
				mapForSim.merge(candidateId, 1, Integer::sum);
			}
			else{
				// Sim < 0.6 (lowestConsideredSimValue)
			}
		}
		side.entrySet().forEach(i -> System.out.println(i.getKey() + ": " + i.getValue().size()));
		return side;
	}
	
	private static Map<BigDecimal, Map<String, Integer>> initializeLinkStatusMap(Map<BigDecimal, Map<String, Integer>> linkStatusMap){		
		for (BigDecimal i = HIGHEST_SIMILARITY; i.compareTo(LOWEST_SIMILARITY) >= 0; i = i.subtract(STEP_LENGTH)){
//			System.out.println(i);
			linkStatusMap.put(i, new HashMap<String, Integer>());
		}
		return linkStatusMap;
	}
	
	private static Map<Boolean, Integer> getSimpleCountMap(Map<String, Integer> completeMap){
		final Map<Boolean, Integer> countMap = new HashMap<Boolean, Integer>();
		completeMap.forEach((k, v) -> {
			final boolean isOneToOneLink = (v == 1);
			countMap.merge(isOneToOneLink, 1, Integer::sum);
		});
		System.out.println(countMap);
		return countMap;
	}
	
	private static Map<Integer, Integer> getCountMap(Map<String, Integer> completeMap){
		final Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
		completeMap.forEach((k, v) -> countMap.merge(v, 1, Integer::sum));
		System.out.println(countMap);
		return countMap;
	}
	
	private static Map<String, Integer> getCompleteMap(Map<BigDecimal, Map<String, Integer>> linkStatusMap){
		final Collection<Map<String, Integer>> maps = linkStatusMap.values();
		final Map<String, Integer> completeMap = new HashMap<String, Integer>();
		for (Map<String, Integer> simMap : maps){
			simMap.forEach((k, v) -> completeMap.merge(k, v, Integer::sum));
		}
//		System.out.println(completeMap);
		return completeMap;
	}
	
	
	private static BigDecimal roundSimilarity(BigDecimal similarity){
		final BigDecimal two = new BigDecimal("2");
		
		return similarity
			.multiply(two)
			.setScale(1, RoundingMode.DOWN)
			.divide(two)
			.setScale(2, RoundingMode.UNNECESSARY);
	}
	*/
}