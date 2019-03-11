package dbs.pprl.toolbox.client.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import dbs.pprl.toolbox.client.preprocessing.FieldMerger;
import dbs.pprl.toolbox.client.preprocessing.FieldNormalizer;
import dbs.pprl.toolbox.client.preprocessing.FieldPruner;
import dbs.pprl.toolbox.client.preprocessing.FieldSplitter;
import dbs.pprl.toolbox.client.preprocessing.Preprocessor;
import dbs.pprl.toolbox.client.preprocessing.fieldmerger.MergeDefinition;
import dbs.pprl.toolbox.client.preprocessing.fieldmerger.Merger;
import dbs.pprl.toolbox.client.preprocessing.fieldnormalizer.NormalizeDefinition;
import dbs.pprl.toolbox.client.preprocessing.fieldnormalizer.Normalizer;
import dbs.pprl.toolbox.client.preprocessing.fieldpruner.PruneDefinition;
import dbs.pprl.toolbox.client.preprocessing.fieldsplitter.SplitDefinition;
import dbs.pprl.toolbox.client.preprocessing.fieldsplitter.Splitter;
import dbs.pprl.toolbox.utils.ClassNameObjectConverter;

public class ConfigLoaderPreprocessing {
	
	public static final String CONFIG = "preprocessing.properties";
	public static final String PATH = "/home/mfranke/workspace/toolbox-pprl/" + CONFIG;
	
	public static final String SPLITTER_DIR = "dbs.pprl.toolbox.client.preprocessing.fieldsplitter";
	public static final String MERGER_DIR = "dbs.pprl.toolbox.client.preprocessing.fieldmerger";
	public static final String NORMALIZER_DIR = "dbs.pprl.toolbox.client.preprocessing.fieldnormalizer";
	
	public static final String SPLITTER = "split";
	public static final String MERGER = "merge";
	public static final String PRUNER = "prune";
	public static final String NORMALIZER = "normalize";

	private final PropertiesConfiguration config;
		
	public ConfigLoaderPreprocessing() throws ConfigurationException{
		this(PATH);
	}
		
	public ConfigLoaderPreprocessing(String path) throws ConfigurationException{
		final Configurations configs = new Configurations();
		this.config = configs.properties(new File(path));	
		/*
		final FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
		        new FileBasedConfigurationBuilder<PropertiesConfiguration>(
		                PropertiesConfiguration.class)
		                .configure(new Parameters().properties()
		                		.setFile(new File(path))
		                        .setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
		                        .setThrowExceptionOnMissing(false));
		this.config = builder.getConfiguration();
		*/
	}
	
	public List<Preprocessor> load() throws Exception{
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
		
	public static void main(String[] args) throws Exception{
		final ConfigLoaderPreprocessing loader = new ConfigLoaderPreprocessing();
		loader.load();
	}
	
}
