package dbs.pprl.toolbox.client.preprocessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dbs.pprl.toolbox.client.common.CSVInputFile;
import dbs.pprl.toolbox.client.common.Task;
import dbs.pprl.toolbox.client.common.config.ConfigLoaderPreprocessing;
import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.Preprocessor;

/**
 * Class for executing a defined pre-processing workflow, i. e.,
 * multiple {@link Preprocessor}s successively.
 * @author mfranke
 *
 */
public class PreprocessingTask extends Task{
	
	public static final String CONFIG_FILE_NAME = "preprocessing.properties";
	public static final String PATH = "/home/mfranke/workspace/toolbox-pprl/" + CONFIG_FILE_NAME;
	public static final String TASK_NAME = "preproc";
	
	private final List<Preprocessor> preprocessors;
	
	public static PreprocessingTask fromConfig(String pathToConfig) throws Exception {
		final ConfigLoaderPreprocessing confLoader = new ConfigLoaderPreprocessing(pathToConfig);
		return confLoader.build();
	}
		
	public PreprocessingTask(CSVInputFile cSVInputFile){
		super(cSVInputFile);
		this.preprocessors = new ArrayList<Preprocessor>();
	}
	
	public void addPreprocessor(Preprocessor... preprocessors){
		for (final Preprocessor preprocessor : preprocessors){
			this.preprocessors.add(preprocessor);
		}
	}
	
	public void addPreprocessor(List<Preprocessor> preprocessors){
		for (final Preprocessor preprocessor : preprocessors){
			this.preprocessors.add(preprocessor);
		}
	}
	
	@Override
	public String getTaskName() {
		return TASK_NAME;
	}	
	
	@Override
	public void execute() throws IOException, AttributeParseException {
		final List<Record> records = this.readFile();
		
		for (final Preprocessor preprocessor : this.preprocessors){
			preprocessor.apply(records, this.inputFileHeader);
		}
		
		this.writeFile(records);
	}
	
	public static void main(String[] args) throws Exception{
		
		final PreprocessingTask preprocessingTask = PreprocessingTask.fromConfig(PATH);
		preprocessingTask.execute();
		
		/*
		// OR MANUAL SETUP LIKE
//		-------------------------------------------------------------------
		
		final MergeDefinition mergeDef = new MergeDefinition();
		mergeDef.setMerger(new Integer[]{3, 4}, new DefaultMerger(""));
//		final FieldMerger merger = new FieldMerger(mergeDef);
//		preprocessing.addPreprocessor(merger);
		
//		-------------------------------------------------------------------
		
		final SplitDefinition splitDef = new SplitDefinition();
//		splitDef.setSplitter(4, new DotSplitter(3));
		splitDef.setSplitter(3, new PositionSplitter(2));
//		final FieldSplitter splitter = new FieldSplitter(splitDef);	
//		preprocessing.addPreprocessor(splitter);
		
//		-------------------------------------------------------------------

		final PruneDefinition pruneDef = new PruneDefinition();
		pruneDef.add(4);
		final FieldPruner pruner = new FieldPruner(pruneDef);
		preprocessing.addPreprocessor(pruner);
//		-------------------------------------------------------------------

		final Normalizer standardStringNormalizer = new StandardStringNormalizer();
//		final Normalizer zipNormalizer = new StandardNumberNormalizer();

		final NormalizerChain cityNormalizer = new NormalizerChain();
		cityNormalizer.add(standardStringNormalizer);
		cityNormalizer.add(new SubstringNormalizer(0, 12));	
		
		final NormalizeDefinition norm = new NormalizeDefinition();
//		norm.setNormalizer(1, standardStringNormalizer);
//		norm.setNormalizer(2, standardStringNormalizer);
//		norm.setNormalizer(3, zipNormalizer);
//		norm.setNormalizer(4, cityNormalizer);
//		norm.setNormalizer(5, new PunctuationRemover());
//		norm.setNormalizer(1, standardStringNormalizer);
//		norm.setNormalizer(2, standardStringNormalizer);
//		norm.setNormalizer(3, standardStringNormalizer);
		norm.setNormalizer(4, new PunctuationRemover());
		
		final FieldNormalizer normalizer = new FieldNormalizer(norm);
		preprocessing.addPreprocessor(normalizer);
//		-------------------------------------------------------------------
		*/
	}

}