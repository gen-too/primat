package dbs.pprl.toolbox.client.preprocessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import dbs.pprl.toolbox.client.common.Task;
import dbs.pprl.toolbox.client.common.config.PreprocessingConfig;
import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.Preprocessor;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldmerger.FieldMerger;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldmerger.MergeDefinition;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer.FieldNormalizer;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer.NormalizeDefinition;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldpruner.FieldPruner;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldpruner.PruneDefinition;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldsplitter.FieldSplitter;
import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldsplitter.SplitDefinition;

/**
 * Class for executing a defined pre-processing workflow, i. e.,
 * multiple {@link Preprocessor}s successively.
 * @author mfranke
 *
 */
@Service
@Import(PreprocessingConfig.class)
public class PreprocessingTask extends Task{
	
	public static final String TASK_NAME = "preproc";
	
	private final List<Preprocessor> preprocessors;
	
	@Autowired
	public PreprocessingTask(PreprocessingConfig preprocessingConfig){
		super(preprocessingConfig.getCsvInputFileConfig());
		System.out.println("F: " + preprocessingConfig.getColumnsToPrune());
//		System.out.println("F: " + preprocessingConfig.getSplite().get(0);
//		System.out.println("F: " + Arrays.toString(preprocessingConfig.getMergee().get(0).getColumns()));

		
		this.preprocessors = new ArrayList<Preprocessor>();
		
		final PruneDefinition pruneDef = preprocessingConfig.getPruneDefinition();
		final SplitDefinition splitDef = preprocessingConfig.getSplitDefinition();
		final MergeDefinition mergeDef = preprocessingConfig.getMergeDefinition();
		final NormalizeDefinition normDef = preprocessingConfig.getNormalizeDefinition();
		
		final FieldPruner fieldPruner = new FieldPruner(pruneDef);
		final FieldSplitter fieldSplitter = new FieldSplitter(splitDef);
		final FieldMerger fieldMerger = new FieldMerger(mergeDef);
		final FieldNormalizer fieldNormalizer = new FieldNormalizer(normDef);
		
		this.preprocessors.add(fieldPruner);
		this.preprocessors.add(fieldSplitter);
		this.preprocessors.add(fieldMerger);
		this.preprocessors.add(fieldNormalizer);
	}
		
	@Override
	public String getTaskName() {
		return TASK_NAME;
	}	
	
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PreprocessingTask [preprocessors=");
		builder.append(preprocessors);
		builder.append("]");
		return builder.toString();
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
		final AnnotationConfigApplicationContext applicationContext = 
        		new AnnotationConfigApplicationContext(PreprocessingTask.class);
		
        final PreprocessingTask preprocessingTask = applicationContext.getBean(PreprocessingTask.class);
        System.out.println(preprocessingTask);
//        preprocessingTask.execute();        
        applicationContext.close();
	}

}