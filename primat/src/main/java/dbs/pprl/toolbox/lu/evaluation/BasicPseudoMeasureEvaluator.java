package dbs.pprl.toolbox.lu.evaluation;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import dbs.pprl.toolbox.lu.classification.CandidatePairGrouper;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;

/**
 * 
 * @author mfranke
 *
 */
public final class BasicPseudoMeasureEvaluator extends PseudoMeasureEvaluationComponent{

	private final BigDecimal recordsPartyA;
	private final BigDecimal recordsPartyB;
		
	private Map<EncodedRecord, Long> leftNodes;
	private Map<EncodedRecord, Long> rightNodes;
	private Map<Boolean, Long> leftCountMap;
	private Map<Boolean, Long> rightCountMap;
	
	private BigDecimal numberOfClassifiedMatches;
	private BigDecimal leftNodeCount;
	private BigDecimal rightNodeCount;
	private BigDecimal leftOneToOne;
	private BigDecimal rightOneToOne;
	private BigDecimal leftMultiLinks;
	private BigDecimal rightMultiLinks;
	
	private BigDecimal pseudoRecall1;
	private BigDecimal pseudoPrecision1;
	private BigDecimal pseudoFMeasure1;
	
	private BigDecimal pseudoRecall2;
	private BigDecimal pseudoPrecision2;
	private BigDecimal pseudoFMeasure2;
	
	private BigDecimal pseudoRecall3;
	private BigDecimal pseudoPrecision3;
	private BigDecimal pseudoFMeasure3;
	
	private BigDecimal oneToOneLinkRatio;
	private BigDecimal multiLinkRatio;
	 
	public BasicPseudoMeasureEvaluator(int recordsPartyA, int recordsPartyB) {
		this.recordsPartyA = new BigDecimal(recordsPartyA);
		this.recordsPartyB = new BigDecimal(recordsPartyB);
	}
	
	@Override
	public void evaluatePseudoQualityMetricsConcrete(Set<CandidatePairWithSimilarity> classifiedMatches) {
		this.evaluateClassifiedMatches(classifiedMatches);
		this.obtainMetrics(classifiedMatches);
		this.addMetrics();
	}


	private void evaluateClassifiedMatches(Set<CandidatePairWithSimilarity> classifiedMatches) {
		final CandidatePairGrouper grouper = new CandidatePairGrouper(classifiedMatches);

		if (this.parallelExecution){	
			this.leftNodes = grouper.groupLeftAndCountParallel();
			this.rightNodes = grouper.groupRightAndCountParallel();
			this.leftCountMap = grouper.groupLeftAndCountLinksByTypeParallel(leftNodes);
			this.rightCountMap = grouper.groupRightAndCountLinksByTypeParallel(rightNodes);
		}
		else{
			this.leftNodes = grouper.groupLeftAndCountSequential();
			this.rightNodes = grouper.groupRightAndCountSequential();
			this.leftCountMap = grouper.groupLeftAndCountLinksByTypeSequential(leftNodes);
			this.rightCountMap = grouper.groupRightAndCountLinksByTypeSequential(rightNodes);
		}		
	}
	
	private void obtainMetrics(Set<CandidatePairWithSimilarity> classifiedMatches){	
		this.numberOfClassifiedMatches = new BigDecimal(classifiedMatches.size());
		
		this.leftNodeCount = new BigDecimal(this.leftNodes.size());
		this.rightNodeCount = new BigDecimal(this.rightNodes.size());
		
		if (this.leftCountMap.containsKey(Boolean.TRUE)){
			this.leftOneToOne = new BigDecimal(this.leftCountMap.get(Boolean.TRUE));
		}
		else{
			this.leftOneToOne = BigDecimal.ZERO;
		}
		
		if (this.rightCountMap.containsKey(Boolean.TRUE)){
			this.rightOneToOne = new BigDecimal(this.rightCountMap.get(Boolean.TRUE));
		}
		else{
			this.rightOneToOne = BigDecimal.ZERO;
		}
		
		if (this.leftCountMap.containsKey(Boolean.FALSE)){
			this.leftMultiLinks = new BigDecimal(this.leftCountMap.get(Boolean.FALSE));
		}
		else{
			this.leftMultiLinks = BigDecimal.ZERO;
		}
		
		if (this.rightCountMap.containsKey(Boolean.FALSE)){
			this.rightMultiLinks = new BigDecimal(this.rightCountMap.get(Boolean.FALSE));
		}
		else{
			this.rightMultiLinks = BigDecimal.ZERO;
		}
		
		this.pseudoRecall1 = this.calculcatePseudoRecall1();
		this.pseudoPrecision1 = this.calculcatePseudoPrecision1();
		this.pseudoFMeasure1 = calculcatePseudoFMeasure(pseudoRecall1, pseudoPrecision1);
		
		this.pseudoRecall2 = this.calculcatePseudoRecall2();
		this.pseudoPrecision2 = this.calculcatePseudoPrecision2();
		this.pseudoFMeasure2 = calculcatePseudoFMeasure(pseudoRecall2, pseudoPrecision2);
		
		this.pseudoRecall3 = this.calculcatePseudoRecall3();
		this.pseudoPrecision3 = this.calculcatePseudoPrecision3();
		this.pseudoFMeasure3 = calculcatePseudoFMeasure(pseudoRecall3, pseudoPrecision3);
		
		this.oneToOneLinkRatio = calculateOneToOneLinkRatio();		
		this.multiLinkRatio = this.calculateMultiLinkRatio();
	}
	
	private void addMetrics(){
		this.metrics.put(LEFT_NODES, this.leftNodeCount);
		this.metrics.put(RIGHT_NODES, this.rightNodeCount);
			
		this.metrics.put(LEFT_ONE_TO_ONE_LINKS, this.leftOneToOne);
		this.metrics.put(RIGHT_ONE_TO_ONE_LINKS, this.rightOneToOne);
					
		this.metrics.put(LEFT_MULTI_LINKS, this.leftMultiLinks);
		this.metrics.put(RIGHT_MULTI_LINKS, this.rightMultiLinks);
				
		this.metrics.put(PSEUDO_RECALL_1, pseudoRecall1);
		this.metrics.put(PSEUDO_PRECISION_1, pseudoPrecision1);
		this.metrics.put(PSEUDO_F_MEASURE_1, pseudoFMeasure1);
		
		this.metrics.put(PSEUDO_RECALL_2, pseudoRecall2);
		this.metrics.put(PSEUDO_PRECISION_2, pseudoPrecision2);
		this.metrics.put(PSEUDO_F_MEASURE_2, pseudoFMeasure2);
		
		this.metrics.put(PSEUDO_RECALL_3, pseudoRecall3);
		this.metrics.put(PSEUDO_PRECISION_3, pseudoPrecision3);
		this.metrics.put(PSEUDO_F_MEASURE_3, pseudoFMeasure3);
		
		this.metrics.put(ONE_TO_ONE_LINK_RATIO, oneToOneLinkRatio);
		this.metrics.put(MULTI_LINK_RATIO, multiLinkRatio);
	}
	
	private BigDecimal calculateOneToOneLinkRatio(){
		final BigDecimal nom = leftOneToOne.add(rightOneToOne);
		final BigDecimal denom = nom.add(rightMultiLinks).add(leftMultiLinks);
		final BigDecimal oneToOneLinkRatio = MetricFormat.divide(nom, denom);
		return oneToOneLinkRatio;
	}
	
	private BigDecimal calculateMultiLinkRatio(){
		final BigDecimal nom = leftMultiLinks.add(rightMultiLinks);
		final BigDecimal denom = nom.add(leftOneToOne).add(rightOneToOne);
		final BigDecimal multiLinkRatio = MetricFormat.divide(nom,denom);
		return multiLinkRatio;
	}
	
	private BigDecimal calculcatePseudoRecall1(){
		final BigDecimal nom = numberOfClassifiedMatches;
		final BigDecimal denom = this.recordsPartyA.min(this.recordsPartyB);
		final BigDecimal pseudoRecall = MetricFormat.divide(nom, denom);
		return pseudoRecall;
	}
	
	private BigDecimal calculcatePseudoPrecision1(){
		final BigDecimal nom = leftNodeCount;
		final BigDecimal denom = numberOfClassifiedMatches;
		//TODO: check division by zero.
		final BigDecimal pseudoPrecision = MetricFormat.divide(nom, denom);
		return pseudoPrecision;
	}
	
	private BigDecimal calculcatePseudoRecall2(){
		final BigDecimal nom = leftNodeCount.add(rightNodeCount);
		final BigDecimal denom = this.recordsPartyA.add(this.recordsPartyB);
		final BigDecimal pseudoRecall = MetricFormat.divide(nom, denom);
		return pseudoRecall;
	}
	
	private BigDecimal calculcatePseudoPrecision2(){
		final BigDecimal nom = leftNodeCount.add(rightNodeCount);
		final BigDecimal denom = numberOfClassifiedMatches.multiply(new BigDecimal("2"));
		final BigDecimal pseudoPrecision = MetricFormat.divide(nom, denom);
		return pseudoPrecision;
	}
	
	private BigDecimal calculcatePseudoRecall3(){
		final BigDecimal nom = leftNodeCount.min(rightNodeCount);
		final BigDecimal denom = this.recordsPartyA.min(this.recordsPartyB);
		final BigDecimal pseudoRecall = MetricFormat.divide(nom, denom);
		return pseudoRecall;
	}
	
	private BigDecimal calculcatePseudoPrecision3(){
		final BigDecimal nom = leftNodeCount.min(rightNodeCount);
		final BigDecimal denom = numberOfClassifiedMatches;
		final BigDecimal pseudoPrecision = MetricFormat.divide(nom, denom);
		return pseudoPrecision;
	}
	
	private static BigDecimal calculcatePseudoFMeasure(BigDecimal recall, BigDecimal precision){
		final BigDecimal nom = new BigDecimal("2").multiply(recall).multiply(precision);
		final BigDecimal denom = recall.add(precision);
		final BigDecimal pseudoFMeasure = MetricFormat.divide(nom, denom);
		return pseudoFMeasure;
	}
}