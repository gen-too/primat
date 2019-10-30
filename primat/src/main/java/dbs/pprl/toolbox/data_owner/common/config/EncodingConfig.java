package dbs.pprl.toolbox.data_owner.common.config;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import dbs.pprl.toolbox.data_owner.encoding.CLKBloomFilterEncoder;
import dbs.pprl.toolbox.data_owner.encoding.Encoder;
import dbs.pprl.toolbox.data_owner.encoding.FBFBloomFilterEncoder;
import dbs.pprl.toolbox.data_owner.encoding.FeatureExtraction;
import dbs.pprl.toolbox.data_owner.encoding.bloomfilter.DoubleHashing;
import dbs.pprl.toolbox.data_owner.encoding.bloomfilter.HashFunctionDefinition;
import dbs.pprl.toolbox.data_owner.encoding.bloomfilter.HashingMethod;
import dbs.pprl.toolbox.data_owner.encoding.bloomfilter.RandomHashing;
import dbs.pprl.toolbox.data_owner.encoding.extractor.ExtractorDefinition;
import dbs.pprl.toolbox.data_owner.encoding.extractor.FeatureExtractor;
import dbs.pprl.toolbox.data_owner.encoding.extractor.FeatureExtractorChain;
import dbs.pprl.toolbox.utils.ClassNameObjectConverter;

@Configuration // This class is a configuration class, i.e. contains info how beans are created for spring context
@PropertySource("encoding.properties")
@ConfigurationProperties
@Import(CSVInputFileConfig.class)
public class EncodingConfig {

	@Autowired
	private CSVInputFileConfig csvInputFileConfig;
    
    private ExtractorDefinition extractorDef;
    
    
    private static FeatureExtractor featureExtractorfromString(String val) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
    	final String[] valComponents = val.split(":");
    	
    	final String featExName = valComponents[0];
    	final String[] ctorParams;
    	
    	if (valComponents.length > 1) {
    		ctorParams = valComponents[1].split(",");
    	}
    	else {
    		ctorParams = new String[0];
    	}
    						
		final Object obj = 
				ClassNameObjectConverter.getObject("dbs.pprl.toolbox.client.encoding.extractor", featExName, ctorParams);
		
		final FeatureExtractor featEx = (FeatureExtractor) obj;
		return featEx;
    }
    
    @Value("#{${columnExtractors}}")
    public void setColumnExtractors(Map<Integer, List<String>> columnExtractors) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
    	this.extractorDef = new ExtractorDefinition();
    	
    	for (final Entry<Integer, List<String>> ex : columnExtractors.entrySet()) {
    		final int col = ex.getKey();
    		final List<String> featExtractors = ex.getValue();
    		
    		if (featExtractors.size() == 1) {
    			this.extractorDef.setExtractor(col, featureExtractorfromString(featExtractors.get(0)));
    		}
    		else {
    			final FeatureExtractorChain featExChain = new FeatureExtractorChain();
    			for (final String featEx : featExtractors) {
    				featExChain.add(featureExtractorfromString(featEx));
    			}
    			this.extractorDef.setExtractor(col, featExChain);
    		}
    	}
    }
    
    public ExtractorDefinition getColumnExtractors() {
    	return this.extractorDef;
    }
    
    private HashFunctionDefinition hashFunctionsDef;
    
    @Value("#{${columnHashes}}")
    public void setHashFunctionDefition(Map<Integer, Integer> columnHashes) {
    	this.hashFunctionsDef = new HashFunctionDefinition();
    	
    	for (final Entry<Integer, Integer> h : columnHashes.entrySet()) {
    		this.hashFunctionsDef.setHashFunctions(h.getKey(), h.getValue());
    	}
    }
    
    public void setHashFunctionDefinition(HashFunctionDefinition hashFuncDef) {
    	this.hashFunctionsDef = hashFuncDef;
    }
    
    public HashFunctionDefinition getHashFunctionDefinition(){
    	return this.hashFunctionsDef;
    }
    
    
    
    private HashingMethod hasher;
    
    private Encoder encoder;
    
    @Value("${bfSize:1024}")
    private int bfSize;
    
    @Value("${hasher}")
    public void setHasher(String hasher) {
    	if (hasher.equals(RandomHashing.class.getSimpleName())) {
    		this.hasher = new RandomHashing(this.bfSize);
    	}
    	else if (hasher.equals(DoubleHashing.class.getSimpleName())) {
    		this.hasher = new DoubleHashing(this.bfSize);
    	}
    	else{
    		throw new IllegalArgumentException();
    	}
    }
    
    public void setHasher(HashingMethod hasher) {
    	this.hasher = hasher;
    }
    
	public CSVInputFileConfig getCsvInputFileConfig() {
		return csvInputFileConfig;
	}

	public void setCsvInputFileConfig(CSVInputFileConfig csvInputFileConfig) {
		this.csvInputFileConfig = csvInputFileConfig;
	}


	public Encoder getEncoder() {
		return encoder;
	}

	@Value("${encoding}")
	public void setEncoder(String encoder) {
		if (encoder.equals(CLKBloomFilterEncoder.class.getSimpleName())) {
			this.encoder = 
				new CLKBloomFilterEncoder(
					this.bfSize,
					new FeatureExtraction(this.extractorDef),
					this.hashFunctionsDef,
					this.hasher
				);
		}
		else if (encoder.equals(FBFBloomFilterEncoder.class.getSimpleName())) {
			this.encoder = 
				new FBFBloomFilterEncoder(
						this.bfSize,
						new FeatureExtraction(this.extractorDef),
						this.hashFunctionsDef,
						this.hasher
				);
		}
		else{
    		throw new IllegalArgumentException();
    	}
	}

	public void setEncoder(Encoder encoder) {
		this.encoder = encoder;
	}
	
	public int getBfSize() {
		return bfSize;
	}

	public void setBfSize(int bfSize) {
		this.bfSize = bfSize;
	}

	public HashingMethod getHasher() {
		return hasher;
	}

	public static void main(String[] args) {
        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(EncodingConfig.class);
        EncodingConfig a = applicationContext.getBean(EncodingConfig.class);
       
        System.out.println(a.csvInputFileConfig.getColumnType());
//        System.out.println(a.getHashFunctionDefinition().getColumnToHashFunctionsMapping());
//        System.out.println(((FeatureExtractorChain) a.getColumnExtractors().getExtractor(1)).getFeatureExtractors().get(1));
//        System.out.println(a.bfSize);
//        System.out.println(((BasicBloomFilterEncoder) a.encoder).getHashingMethod());
//        System.out.println(a.hasher.getBfSize());
        applicationContext.close();
    }
}