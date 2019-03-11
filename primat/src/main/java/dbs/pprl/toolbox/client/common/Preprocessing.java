package dbs.pprl.toolbox.client.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;
import dbs.pprl.toolbox.client.preprocessing.Preprocessor;
import dbs.pprl.toolbox.utils.CSVReader;
import dbs.pprl.toolbox.utils.CSVWriter;

public class Preprocessing {

	public static final int DEFAULT_ID_COLUMN = 0;
	public static final String OUTPUT_SUFFIX = ".csv";
	
	private final String pathToFile;
	private final boolean hasHeader;
	private final int idColumn;
	
	private final List<Preprocessor> preprocessors;
	
	private LinkedList<String> header;
	
	public Preprocessing(String pathToFile, boolean hasHeader){
		this(pathToFile, hasHeader, DEFAULT_ID_COLUMN);
	}
	
	public Preprocessing(String pathToFile, boolean hasHeader, int idColumn){
		this.pathToFile = pathToFile;
		this.hasHeader = hasHeader;
		this.idColumn = idColumn;
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
	
	private List<CSVRecordEntry> readFile() throws IOException{
		final CSVReader csvReader = new CSVReader(pathToFile, hasHeader);
		this.header = csvReader.getHeader();
		final List<CSVRecord> csvRecords = csvReader.read();
		return RecordEntryTransformer.from(csvRecords, this.idColumn);
	}
	
	
	private void writeFile(List<CSVRecordEntry> records) throws IOException{
		final CSVWriter csvWriter = new CSVWriter(this.getOutputPath());
		if (this.header != null && !this.header.isEmpty()){
			csvWriter.writeCSVRecordsWithHeader(records, this.header);
		}
		else{
			csvWriter.writeCSVRecords(records);
		}
	}
	
	private String getOutputPath(){
		final String[] pathComponents = this.pathToFile.split("\\.");
		StringBuilder builder = new StringBuilder();
		builder.append(pathComponents[0]);
		builder.append("_p");
		builder.append(OUTPUT_SUFFIX);
		return builder.toString();
	}
	
	public void preprocess() throws IOException{
		final List<CSVRecordEntry> records = this.readFile();
		
		for (final Preprocessor preprocessor : this.preprocessors){
			preprocessor.apply(records, this.header);
		}
		
		this.writeFile(records);
	}
	
	public static void main(String[] args) throws Exception{
		final String file = "/home/mfranke/Schreibtisch/M1.csv";
		final boolean hasHeader = true;
		final int idColumn = 0;
		
		final Preprocessing preprocessing = new Preprocessing(file, hasHeader, idColumn);

		final ConfigLoaderPreprocessing confLoader = new ConfigLoaderPreprocessing();
		final List<Preprocessor> preprocessors = confLoader.load();
		
		preprocessing.addPreprocessor(preprocessors);
		
		/*
		// OR
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
		
		preprocessing.preprocess();
	}
	
	
}
