package dbs.pprl.toolbox.client.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration2.ex.ConfigurationException;

import dbs.pprl.toolbox.client.common.CSVInputFile;
import dbs.pprl.toolbox.client.preprocessing.PreprocessingTask;
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
 * Load property-file for pre-processing and build a {@link Preprocessing} Workflow. 
 * 
 * @author mfranke
 *
 */
public class ConfigLoaderPreprocessing extends ConfigLoader{
		
	public static final String SPLITTER_DIR = "dbs.pprl.toolbox.client.preprocessing.fieldsplitter";
	public static final String MERGER_DIR = "dbs.pprl.toolbox.client.preprocessing.fieldmerger";
	public static final String NORMALIZER_DIR = "dbs.pprl.toolbox.client.preprocessing.fieldnormalizer";
		
	public static final String SPLITTER = "split";
	public static final String MERGER = "merge";
	public static final String PRUNER = "prune";
	public static final String NORMALIZER = "normalize";
				
	/**
	 * 
	 * @param path Path to config file.
	 * @throws ConfigurationException
	 */
	public ConfigLoaderPreprocessing(String path) throws ConfigurationException{
		super(path);
	}
	
	public PreprocessingTask build() throws Exception {
		final CSVInputFile cSVInputFile = this.getInputFile();
		final PreprocessingTask preprocessingTask =  new PreprocessingTask(cSVInputFile);
		final List<Preprocessor> preprocessors = this.getPreprocessors();
		preprocessingTask.addPreprocessor(preprocessors);
		return preprocessingTask;
	}
		
	private List<Preprocessor> getPreprocessors() throws Exception{
		final List<Preprocessor> preprocessors = new ArrayList<Preprocessor>(); 
		
		final SplitDefinition splitDef = this.getSplitDefinition();
		if (splitDef != null){
			final FieldSplitter splitter = new FieldSplitter(splitDef);
			preprocessors.add(splitter);
		}
		
		final MergeDefinition mergeDef = this.getMergeDefinition();
		if (mergeDef != null){
			final FieldMerger merger = new FieldMerger(mergeDef);
			preprocessors.add(merger);
		}
		
		final PruneDefinition pruneDef = this.getPruneDefinition();
		if (pruneDef != null){
			final FieldPruner pruner = new FieldPruner(pruneDef);
			preprocessors.add(pruner);
		}
		
		final NormalizeDefinition normDef = this.getNormalizeDefinition();	
		if (normDef != null){
			final FieldNormalizer normalizer = new FieldNormalizer(normDef);
			preprocessors.add(normalizer);
		}
				
		return preprocessors;
	}
	
	//TODO: Should we support to read/apply custom normalizer chains?
	// seems difficult and gui would be nicer than config file
	// same as for the others --> i could run preprocessing multiple times to achieve such things
	private NormalizeDefinition getNormalizeDefinition() throws Exception{
		final String[] norms = this.config.getStringArray(NORMALIZER);
		
		if (norms == null || norms.length < 1){
			return null;
		}
		
		final NormalizeDefinition normDef = new NormalizeDefinition();
		
		for (final String norm : norms){
			final String[] params = norm.split(",");
			
			if (params != null && params.length >= 2){
				final int column = Integer.parseInt(params[0]);
				final String className = params[1];
				final String[] ctorParams = Arrays.copyOfRange(params, 2, params.length);
				
				final Object obj = ClassNameObjectConverter.getObject(NORMALIZER_DIR, className, ctorParams);
				
				final Normalizer normalizer = (Normalizer) obj;
				normDef.setNormalizer(column, normalizer);
			}
			else{
				throw new IllegalArgumentException();
			}
		}
		
		return normDef;
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