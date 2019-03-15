package dbs.pprl.toolbox.lu.matching;

import java.io.File;
import java.lang.reflect.Constructor;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import dbs.pprl.toolbox.lu.blocking.Blocker;
import dbs.pprl.toolbox.lu.blocking.metricSpace.MetricSpace;
import dbs.pprl.toolbox.lu.blocking.metricSpace.indexing.DynamicIndexer;
import dbs.pprl.toolbox.lu.blocking.metricSpace.indexing.IndexMethod;
import dbs.pprl.toolbox.lu.blocking.metricSpace.indexing.StaticIndexer;
import dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection.DynamicPivotSelector;
import dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection.PivotByEntriesSelector;
import dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection.PivotByRadiusSelector;
import dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection.PivotFullDatasetSelector;
import dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection.PivotFurthestNodeSelector;
import dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection.PivotRandomSelector;
import dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection.PivotSampleSelector;
import dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection.StaticPivotSelector;
import dbs.pprl.toolbox.lu.blocking.standard.StandardBlocker;
import dbs.pprl.toolbox.lu.blocking.standard.lsh.HLshBlocker;
import dbs.pprl.toolbox.lu.blocking.standard.lsh.HLshBlocker.HLshBlockerBuilder;
import dbs.pprl.toolbox.lu.blocking.standard.lsh.JLshBlocker;
import dbs.pprl.toolbox.lu.blocking.standard.lsh.JLshBlocker.JLshBlockerBuilder;
import dbs.pprl.toolbox.lu.classification.Classificator;
import dbs.pprl.toolbox.lu.classification.ThresholdClassificator;
import dbs.pprl.toolbox.lu.distanceFunctions.DistanceFunction;
import dbs.pprl.toolbox.lu.evaluation.IdQualityEvaluator;
import dbs.pprl.toolbox.lu.evaluation.QualityEvaluator;
import dbs.pprl.toolbox.lu.matching.BasicMatcher.BasicMatcherBuilder;
import dbs.pprl.toolbox.lu.postprocessing.Postprocessor;
import dbs.pprl.toolbox.lu.similarityCalculation.SimilarityCalculator;
import dbs.pprl.toolbox.lu.similarityFunctions.JaccardSimilarity;

public class ConfigurationLoader {
	
	public static final String CONFIG = "config.properties";
	public static final String PATH = "/home/mfranke/workspace/toolbox-pprl/" + CONFIG;
	
	public static final String FILE_A = "fileA";
	public static final String FILE_B = "fileB";
	public static final String BIT_VECTOR_SIZE = "bitVectorSize";
	public static final String SIMILARITY_FUNCTION = "similarityFunction";
	public static final String THRESHOLD = "threshold";
	public static final String POSTPROCESSOR = "postprocessor";
	public static final String EXP_MATCHES = "expectedMatches";
	public static final String BLOCKING_METHOD = "blocking.method";
	public static final String LSH_KEYS = "blocking.lsh.keys";
	public static final String LSH_KEY_LENGTH = "blocking.lsh.keyLength";
	public static final String LSH_OPTIMIZATION = "blocking.lsh.optimization";
	public static final String MS_PIVOTS = "blocking.ms.pivots";
	public static final String MS_PIVOT_SAMPLE_SIZE = "blocking.ms.pivotSampleSize";
	public static final String MS_STATIC_PIVOT_SELECTOR = "blocking.ms.staticPivotSelector";
	public static final String MS_DISTANCE_FUNCTION = "blocking.ms.distanceFuncttion";
	public static final String MS_INDEXER = "blocking.ms.indexer";
	public static final String MS_DYNAMIC_PIVOT_SELECTOR = "blocking.ms.dynamicPivotSelector";
	
	public static final String SIMILARITY_FUNCTION_PACKAGE = "dbs.pprl.toolbox.lu.similarityFunction.";
	public static final String POSTPROCESSOR_PACKAGE = "dbs.pprl.toolbox.lu.postprocessing.";
	public static final String DISTANCE_FUNCTION_PACKAGE = "dbs.pprl.toolbox.lu.distanceFunctions.";
		
	public static final double DEFAULT_THRESHOLD = 0.8d;
	public static final Class<?> DEFAULT_SIMILARITY_FUNCTION = JaccardSimilarity.class;

	private final PropertiesConfiguration config;
	
	public ConfigurationLoader() throws ConfigurationException{
		this(PATH);
	}
	
	public ConfigurationLoader(String path) throws ConfigurationException{
		final Configurations configs = new Configurations();
		this.config = configs.properties(new File(path));
	}
		
	public String getFileA(){
		return this.config.getString(FILE_A);
	}
	
	public String getFileB(){
		return this.config.getString(FILE_B);
	}
	
	public Matcher getMatcher() throws Exception{		
		final double threshold = this.config.getDouble(THRESHOLD, DEFAULT_THRESHOLD);
		
		final Blocker blocker = this.getBlocker(threshold);
		final SimilarityCalculator similarityCalculator = this.getSimilarityCalculator();
		final Classificator classificator = new ThresholdClassificator(threshold);
		final Postprocessor postprocessor = this.getPostprocessor();
		final QualityEvaluator qualityEvaluator = this.getQualityEvaluator();
		
		return new BasicMatcherBuilder()
			.setBlocker(blocker)
			.setSimilarityCalculator(similarityCalculator)
			.setClassificator(classificator)
			.setPostprocessor(postprocessor)
			.setQualityEvaluator(qualityEvaluator)
			.build();
	}
	
	private Blocker getBlocker(double threshold) throws Exception{
		final String blockerName = this.config.getString(BLOCKING_METHOD);

		final Blocker blocker;	
		if (blockerName.equals(StandardBlocker.class.getSimpleName())){
			blocker = new StandardBlocker();
		}
		else if (blockerName.equals(HLshBlocker.class.getSimpleName())){
			final int lshKeys = config.getInt(LSH_KEYS);
			final int lshKeyLength = config.getInt(LSH_KEY_LENGTH);
			final boolean lshOptimization = config.getBoolean(LSH_OPTIMIZATION, false);
			final int bitVectorSize = this.config.getInt(BIT_VECTOR_SIZE);
			blocker = 
				new HLshBlockerBuilder()
					.setKeys(lshKeys)
					.setKeySize(lshKeyLength)
					.setValueRange(bitVectorSize)
					.setKeyRestriction(lshOptimization)
					.build();			
		}
		else if (blockerName.equals(JLshBlocker.class.getSimpleName())){
			final int lshKeys = config.getInt(LSH_KEYS);
			final int lshKeyLength = config.getInt(LSH_KEY_LENGTH);
			final int bitVectorSize = this.config.getInt(BIT_VECTOR_SIZE);
			blocker = 
					new JLshBlockerBuilder()
						.setKeys(lshKeys)
						.setKeySize(lshKeyLength)
						.setValueRange(bitVectorSize)
						.build();
		}
		else if (blockerName.equals(MetricSpace.class.getSimpleName())){
			final int numberOfPivots = config.getInt(MS_PIVOTS);
			
			
			final String stringDistFunc = config.getString(MS_DISTANCE_FUNCTION);
			final Class<?> distFuncClass = Class.forName(DISTANCE_FUNCTION_PACKAGE + stringDistFunc);
			final Constructor<?> distFuncCtor = distFuncClass.getConstructor();
			final DistanceFunction distFunction = (DistanceFunction) distFuncCtor.newInstance();
			
			final StaticPivotSelector staticPivotSelector = this.getStaticPivotSelector(numberOfPivots, distFunction);
			
			final IndexMethod indexer;
			final String indexMethodName = config.getString(MS_INDEXER);
			if (indexMethodName.equals(StaticIndexer.class.getSimpleName())){
				indexer = new StaticIndexer(distFunction);
			}
			else if (indexMethodName.equals(DynamicIndexer.class.getSimpleName())){
				final String dynPivotSelectorName = config.getString(MS_DYNAMIC_PIVOT_SELECTOR);
				final DynamicPivotSelector dynPivotSelector;
				
				if (dynPivotSelectorName.equals(PivotByEntriesSelector.class.getSimpleName())){
					dynPivotSelector = new PivotByEntriesSelector(distFunction);
				}
				else if (dynPivotSelectorName.equals(PivotByRadiusSelector.class.getSimpleName())){
					dynPivotSelector = new PivotByRadiusSelector(distFunction);
				}
				else if (dynPivotSelectorName.equals(PivotFurthestNodeSelector.class.getSimpleName())){
					dynPivotSelector = new PivotFurthestNodeSelector(distFunction);
				}
				else{
					throw new ConfigurationParameterException();
				}
				indexer = new DynamicIndexer(distFunction, dynPivotSelector);
			}
			else{
				throw new ConfigurationParameterException();
			}

			blocker = new MetricSpace(staticPivotSelector, indexer, threshold, distFunction);
		}
		else{
			throw new ConfigurationParameterException();
		}
		
		return blocker;
	}
	
	private Postprocessor getPostprocessor() throws Exception{
		final String stringPostprocessor = this.config.getString(POSTPROCESSOR);
		
		if (stringPostprocessor.isEmpty() || stringPostprocessor.equals("None")){
			return null;
		}
		else{
			final Class<?> postprocessorClass = Class.forName(POSTPROCESSOR_PACKAGE + stringPostprocessor);
			final Constructor<?> postprocessorCtor = postprocessorClass.getConstructor();
			return(Postprocessor) postprocessorCtor.newInstance();
		}
	}
	
	private QualityEvaluator getQualityEvaluator(){
		final Long expectedMatches = this.config.getLong(EXP_MATCHES, null);
		
		if (expectedMatches != null){
			return new IdQualityEvaluator(expectedMatches);
		}
		else{
			return null;
		}
	}
	
	private StaticPivotSelector getStaticPivotSelector(int numberOfPivots, DistanceFunction distFunction) throws ConfigurationParameterException{
		final String staticPivotSelectorString = config.getString(MS_STATIC_PIVOT_SELECTOR);
		if (staticPivotSelectorString.equals(PivotRandomSelector.class.getSimpleName())){
			return new PivotRandomSelector(numberOfPivots);
		}
		else if (staticPivotSelectorString.equals(PivotSampleSelector.class.getSimpleName())){
			final int sampleSize = config.getInt(MS_PIVOT_SAMPLE_SIZE);
			return new PivotSampleSelector(numberOfPivots, sampleSize, distFunction);
		}
		else if (staticPivotSelectorString.equals(PivotFullDatasetSelector.class.getSimpleName())){
			return new PivotFullDatasetSelector(numberOfPivots, distFunction);
		}
		else{
			throw new ConfigurationParameterException();
		}
	}
	
	private SimilarityCalculator getSimilarityCalculator() throws Exception {
		final String simCalcString = this.config.getString(SIMILARITY_FUNCTION, null);
		final Class<?> simCalcClass;
		if (simCalcString == null || simCalcString.isEmpty()){
			simCalcClass = DEFAULT_SIMILARITY_FUNCTION;
		}
		else {
			simCalcClass = Class.forName(SIMILARITY_FUNCTION_PACKAGE + simCalcString);
		}
		final Constructor<?> simCalcCtor = simCalcClass.getConstructor();
		return (SimilarityCalculator) simCalcCtor.newInstance();
	}
}
