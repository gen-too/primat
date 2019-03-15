package dbs.pprl.toolbox.client.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration2.ex.ConfigurationException;

import dbs.pprl.toolbox.client.common.CSVInputFile;
import dbs.pprl.toolbox.client.encoding.Encoder;
import dbs.pprl.toolbox.client.encoding.EncodingTask;
import dbs.pprl.toolbox.client.encoding.FeatureExtraction;
import dbs.pprl.toolbox.client.encoding.bloomfilter.HashFunctionDefinition;
import dbs.pprl.toolbox.client.encoding.bloomfilter.HashingMethod;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.Preprocessor;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldmerger.FieldMerger;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldmerger.MergeDefinition;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldmerger.Merger;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer.FieldNormalizer;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer.NormalizeDefinition;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer.Normalizer;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldpruner.FieldPruner;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldpruner.PruneDefinition;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldsplitter.FieldSplitter;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldsplitter.SplitDefinition;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldsplitter.Splitter;
import dbs.pprl.toolbox.utils.ClassNameObjectConverter;

/**
 * 
 * @author mfranke
 *
 */
public class ConfigLoaderEncoding extends ConfigLoader{
		
				
	/**
	 * 
	 * @param path Path to config file.
	 * @throws ConfigurationException
	 */
	public ConfigLoaderEncoding(String path) throws ConfigurationException{
		super(path);
	}
	
	public EncodingTask build() throws Exception {
		final CSVInputFile cSVInputFile = this.getInputFile();
		
		final Encoder encoder = this.getEncoder();
		final EncodingTask encodingTask =  new EncodingTask(cSVInputFile, encoder);
		
		return encodingTask;
	}
		
	private Encoder getEncoder() {
		final String encoderString = this.config.getString(key)
		
		
		final int bfLength = this.config.getInt("bfLength", 1024);
		
		
		
		int bfLength, FeatureExtraction extractor, HashFunctionDefinition hashFunctions, HashingMethod bfHasher)

	}

	private PruneDefinition getPruneDefinition(){
		final String[] prunes = this.config.getStringArray(PRUNER);
		
		if (prunes == null || prunes.length < 1){
			return null;
		}
		
		final PruneDefinition pruneDef = new PruneDefinition();
		
		for (final String prune : prunes){
			final int column = Integer.parseInt(prune);
			pruneDef.add(column);
		}
		
		return pruneDef;
	}

	//TODO: MergeDef and SplitDef: should we allow to merge/split same columns again? currently it is a map and so ...
	private MergeDefinition getMergeDefinition() throws Exception{
		final String[] merges = this.config.getStringArray(MERGER);
		
		if (merges == null || merges.length < 1){
			return null;
		}
		
		final MergeDefinition mergeDef = new MergeDefinition();
		
		for (final String merge : merges){
			final String[] mergeParams = merge.split(",");
			
			if (mergeParams != null && mergeParams.length >= 3){
				final String className = mergeParams[mergeParams.length-1];
				
				final Integer[] columns = new Integer[mergeParams.length-1];				
				for (int i = 0; i < mergeParams.length-1; i++){
					columns[i] = Integer.parseInt(mergeParams[i]);
				}
							
				final Object obj = ClassNameObjectConverter.getObject(MERGER_DIR, className);
				
				final Merger merger = (Merger) obj;
				mergeDef.setMerger(columns, merger);
			}
			else{
				throw new IllegalArgumentException();
			}
		}
		
		return mergeDef;
	}

	private SplitDefinition getSplitDefinition() throws Exception{
		final String[] splits = this.config.getStringArray(SPLITTER);
		
		if (splits == null || splits.length < 1){
			return null;
		}
		
		final SplitDefinition splitDef = new SplitDefinition();
		
		for (final String split : splits){
			final String[] splitParams = split.split(",", 4);
			
			if (splitParams != null && splitParams.length >= 3){
				final int column = Integer.parseInt(splitParams[0]);
				final String className = splitParams[1];
				
				final String[] ctorParams = Arrays.copyOfRange(splitParams, 2, splitParams.length);
				
				final Object obj = ClassNameObjectConverter.getObject(SPLITTER_DIR, className, ctorParams);
				final Splitter splitter = (Splitter) obj;
				splitDef.setSplitter(column, splitter);
			}
			else{
				throw new IllegalArgumentException();
			}
		}
		
		return splitDef;
	}	
}