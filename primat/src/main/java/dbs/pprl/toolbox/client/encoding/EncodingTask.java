package dbs.pprl.toolbox.client.encoding;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


import dbs.pprl.toolbox.client.common.CSVInputFile;
import dbs.pprl.toolbox.client.common.Task;
import dbs.pprl.toolbox.client.common.config.ConfigLoaderPreprocessing;
import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.data.records.EncodedRecord;
import dbs.pprl.toolbox.client.encoding.bloomfilter.HashFunctionDefinition;
import dbs.pprl.toolbox.client.encoding.bloomfilter.HashingMethod;
import dbs.pprl.toolbox.client.encoding.bloomfilter.RandomHashing;
import dbs.pprl.toolbox.client.encoding.extractor.QGramExtractor;
import dbs.pprl.toolbox.client.preprocessing.PreprocessingTask;
import dbs.pprl.toolbox.client.encoding.extractor.ExtractorDefinition;

public class EncodingTask extends Task {
		
	public static final String CONFIG_FILE_NAME = "encoding.properties";
	public static final String PATH = "/home/mfranke/workspace/toolbox-pprl/" + CONFIG_FILE_NAME;
	public static final String TASK_NAME = "enc";
	
	private final Encoder encoder;
	
	public static PreprocessingTask fromConfig(String pathToConfig) throws Exception {
		final ConfigLoaderPreprocessing confLoader = new ConfigLoaderPreprocessing(pathToConfig);
		return confLoader.build();
	}
	
	public EncodingTask(CSVInputFile csvInputFile, Encoder encoder){
		super(csvInputFile);
		this.encoder = encoder;
	}
	
	@Override
	public void execute() throws RuntimeException, IOException, AttributeParseException {
		final List<Record> records = this.readFile();
		final List<EncodedRecord> encodedRecs = this.encoder.encode(records);		
		this.writeEncodedFile(encodedRecs);		
	}

	@Override
	public String getTaskName() {
		return "enc";
	}
				

	public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException, AttributeParseException{
		final ColumnAttributTypeMapping columnTypeMapping = new ColumnAttributTypeMapping(0);
		columnTypeMapping.setTypeString(1);
		columnTypeMapping.setTypeString(2);
		columnTypeMapping.setTypeString(3);
		columnTypeMapping.setTypeString(4);
//		columnTypeMapping.setTypeString(5);
		
		// -------------------------------------
		final ExtractorDefinition extractorDef = new ExtractorDefinition();
		final boolean padding = true;
		final int q = 3;
		extractorDef.setExtractor(1, new QGramExtractor(q, padding));
		extractorDef.setExtractor(2, new QGramExtractor(q, padding));
		extractorDef.setExtractor(3, new QGramExtractor(q, padding));
		extractorDef.setExtractor(4, new QGramExtractor(q, padding));
//		transDef.setTransformer(5, new QGramTransformer(q, padding));

//		final TransformerChain stc = new TransformerChain();
//		stc.add(new SubstringTransformer(1, 3));
//		stc.add(new SubstringTransformer(2, 4));
//		stc.add(new SubstringTransformer(1, 4));
//		transDef.setTransformer(10, stc);		
		
		final FeatureExtraction featureExtraction = new FeatureExtraction(extractorDef);
// -------------------------------------

		final HashFunctionDefinition hashFunctionsForAttributes = new HashFunctionDefinition();
		final int k = 17;
		hashFunctionsForAttributes.setHashFunctions(1, k);
		hashFunctionsForAttributes.setHashFunctions(2, k);
		hashFunctionsForAttributes.setHashFunctions(3, k);
		hashFunctionsForAttributes.setHashFunctions(4, k);
//		hashFunctionsForAttributes.put(5, k);
		
		final int bfLength = 1024;
		
		final HashingMethod bfHasher = new RandomHashing(bfLength);
		
		
		final Encoder encoder = new CLKBloomFilterEncoder(bfLength, featureExtraction, hashFunctionsForAttributes, bfHasher);
		
// -------------------------------------

		final CSVInputFile inputFile = new CSVInputFile("/home/mfranke/Schreibtisch/M1.csv", true, columnTypeMapping);
		final EncodingTask encodingTask = new EncodingTask(inputFile, encoder);
		encodingTask.execute();
	}
}