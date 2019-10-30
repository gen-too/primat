package dbs.pprl.toolbox.lu.matching;

import java.util.List;
import java.util.Map;
import java.util.Set;

import dbs.pprl.toolbox.lu.blocking.Blocker;
import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.classification.Classificator;
import dbs.pprl.toolbox.lu.evaluation.BasicPseudoMeasureEvaluator;
import dbs.pprl.toolbox.lu.evaluation.QualityEvaluator;
import dbs.pprl.toolbox.lu.postprocessing.Postprocessor;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;
import dbs.pprl.toolbox.lu.similarityCalculation.SimilarityCalculator;
import dbs.pprl.toolbox.utils.ANSICode;

/**
 * 
 * @author mfranke
 * 
 */
public class BasicMatcher extends MatchingComponent{
		
	private Blocker blocker;
	private SimilarityCalculator similarityCalculator;
	private Classificator classificator;
	private Postprocessor postprocessor;
	private QualityEvaluator qualityEvaluator;
	
	private int sizeA;
	private int sizeB;
	
	private List<EncodedRecord> smallerDataset;
	private List<EncodedRecord> biggerDataset;
	
	//TODO: Consider to use Pair- or Tuple-Objects here or wrap them in the corresponding class
	private Set<CandidatePair> candidatePairs;
	private Set<CandidatePairWithSimilarity> candidatePairsWithSimilarity;
	private Set<CandidatePairWithSimilarity> classifiedMatches;
	private Set<CandidatePairWithSimilarity> cleanMatches;
		
	
	private BasicMatcher(BasicMatcherBuilder builder){
		super();
		this.blocker = builder.blocker;
		this.similarityCalculator = builder.similarityCalculator;
		this.classificator = builder.classificator;
		this.postprocessor = builder.postprocessor;
		this.qualityEvaluator = builder.qualityEvaluator;
	}
	
	// Also add default values???
	public static class BasicMatcherBuilder {
		private Blocker blocker;
		private SimilarityCalculator similarityCalculator;
		private Classificator classificator;
		private Postprocessor postprocessor;
		private QualityEvaluator qualityEvaluator;
		
		public BasicMatcherBuilder setBlocker(Blocker blocker){
			this.blocker = blocker;
			return this;
		}
		
		public BasicMatcherBuilder setSimilarityCalculator(SimilarityCalculator similarityCalculator){
			this.similarityCalculator = similarityCalculator;
			return this;
		}
		
		public BasicMatcherBuilder setClassificator(Classificator classificator){
			this.classificator = classificator;
			return this;
		}
		
		public BasicMatcherBuilder setPostprocessor(Postprocessor postprocessor){
			this.postprocessor = postprocessor;
			return this;
		}
		
		public BasicMatcherBuilder setQualityEvaluator(QualityEvaluator qualityEvaluator){
			this.qualityEvaluator = qualityEvaluator;
			return this;
		}
		
		public BasicMatcher build(){
			return new BasicMatcher(this);
		}
	}
	
	
	private void runBlocking(){
		this.candidatePairs = 
			this.blocker.getCandidatePairs(
				this.smallerDataset, 
				this.biggerDataset
			);
		
		this.smallerDataset = null;
		this.biggerDataset = null;
		
		final Map<String, Number> blockingMetrics = this.blocker.getMetrics();
		this.metrics.putAll(blockingMetrics);
		System.out.println("Blocking done. #Candidates: " 
				+ this.candidatePairs.size() 
				+ "\n"
				+ ANSICode.colorize(blockingMetrics.toString(), ANSICode.GREEN)
		);		
	}
	
	private void runSimilarityCalculation(){
		this.candidatePairsWithSimilarity = 
				this.similarityCalculator.calculateSimilarity(this.candidatePairs);
		final Map<String, Number> similarityCalculatorMetrics = this.similarityCalculator.getMetrics();
		this.metrics.putAll(similarityCalculatorMetrics);
		System.out.println("Similarity calculation done. " 
				+ "\n" 
				+ ANSICode.colorize(similarityCalculatorMetrics.toString(), ANSICode.GREEN)
		);
		this.candidatePairs = null;
	}
	
	private void runClassification(){
		this.classifiedMatches = 
				this.classificator.classify(candidatePairsWithSimilarity);
		final Map<String, Number> classificationMetrics = this.classificator.getMetrics();
		this.metrics.putAll(classificationMetrics);
		System.out.println("Classification done. #Classified matches: " 
				+ classifiedMatches.size() 
				+ "\n" 
				+ ANSICode.colorize(classificationMetrics.toString(), ANSICode.GREEN)
		);
		this.candidatePairsWithSimilarity = null;
	}
	
	private void runPostprocessing(){
		if (this.postprocessor != null){			
			this.cleanMatches =
					this.postprocessor.clean(classifiedMatches);
			final Map<String, Number> postprocessingMetrics = this.postprocessor.getMetrics();
			this.metrics.putAll(postprocessingMetrics);
			System.out.println("Postprocessing done. #Clean matches: " 
					+ this.cleanMatches.size()
					+ "\n"
					+ ANSICode.colorize(postprocessingMetrics.toString(), ANSICode.GREEN)
			);	
			this.classifiedMatches = null;
		}
		else{
			this.cleanMatches = this.classifiedMatches;
		}
		this.metrics.put(RESULT_SIZE, this.cleanMatches.size());
	}
	
	private void runPseudoMeasureEvaluation(){
		final BasicPseudoMeasureEvaluator pseudoEvaluator = 
				new BasicPseudoMeasureEvaluator(this.sizeA, this.sizeB);
		pseudoEvaluator.evaluatePseudoQualityMetrics(this.classifiedMatches);
		final Map<String, Number> pseudoMeasures = pseudoEvaluator.getMetrics(); 
		this.metrics.putAll(pseudoMeasures);
		System.out.println(ANSICode.colorize(pseudoMeasures.toString(), ANSICode.GREEN));
	}
	
	private void runBlockingQualityEvaluation(){
		if (this.qualityEvaluator != null){
			this.qualityEvaluator.evaluateBlockingQualityMetrics(this.candidatePairs);
		}
	}
	
	private void runMatchQualityEvaluation(){
		if (this.qualityEvaluator != null){
			this.qualityEvaluator.evaluateMatchQualityMetrics(this.cleanMatches);
		
			final Map<String, Number> qualityMetrics = this.qualityEvaluator.getMetrics();
				
			this.metrics.putAll(qualityMetrics);
			System.out.println(ANSICode.colorize(qualityMetrics.toString(), ANSICode.GREEN));
		}
	}
	
	private void initialize(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB){
		this.sizeA = recordsPartyA.size();
		this.sizeB = recordsPartyB.size();
		
		this.metrics.put(RECORDS_PARTY_A, this.sizeA);
		this.metrics.put(RECORDS_PARTY_B, this.sizeB);
		
		this.smallerDataset = this.sizeA <= this.sizeB ? recordsPartyA : recordsPartyB;
		this.biggerDataset = this.sizeA > this.sizeB ? recordsPartyA : recordsPartyB;
	}
	
	public Set<CandidatePairWithSimilarity> matchConcrete(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB) {
		this.initialize(recordsPartyA, recordsPartyB);	
		
		// TODO Consider to merge blocking, sim. calculation and classification to reduce overhead for collection all candidate pairs
		this.runBlocking();
		
		this.runBlockingQualityEvaluation();
		
		this.runSimilarityCalculation();
		this.runClassification();
		
		this.runPseudoMeasureEvaluation();	
		
		this.runPostprocessing();
		
		this.runMatchQualityEvaluation();
				
		return this.cleanMatches;
	}
	

	public Blocker getBlocker() {
		return blocker;
	}

	public void setBlocker(Blocker blocker) {
		this.blocker = blocker;
	}

	public SimilarityCalculator getSimilarityCalculator() {
		return similarityCalculator;
	}

	public void setSimilarityCalculator(SimilarityCalculator similarityCalculator) {
		this.similarityCalculator = similarityCalculator;
	}

	public Classificator getClassificator() {
		return classificator;
	}

	public void setClassificator(Classificator classificator) {
		this.classificator = classificator;
	}

	public Postprocessor getPostprocessor() {
		return postprocessor;
	}

	public void setPostprocessor(Postprocessor postprocessor) {
		this.postprocessor = postprocessor;
	}

	public QualityEvaluator getQualityEvaluator() {
		return qualityEvaluator;
	}

	public void setQualityEvaluator(QualityEvaluator qualityEvaluator) {
		this.qualityEvaluator = qualityEvaluator;
	}
}