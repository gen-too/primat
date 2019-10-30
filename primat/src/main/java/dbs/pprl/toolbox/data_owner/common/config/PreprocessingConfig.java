package dbs.pprl.toolbox.data_owner.common.config;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldmerger.MergeDefinition;
import dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldmerger.Merger;
import dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer.NormalizeDefinition;
import dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer.Normalizer;
import dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldpruner.PruneDefinition;
import dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldsplitter.SplitDefinition;
import dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldsplitter.Splitter;
import dbs.pprl.toolbox.utils.ClassNameObjectConverter;

@Configuration
@PropertySource("preprocessing.properties")
@ConfigurationProperties
@Import({CSVInputFileConfig.class})
public class PreprocessingConfig {

	@Autowired
	private CSVInputFileConfig csvInputFileConfig;

	private Set<Integer> columnsToPrune;
	
	private Map<Integer, SplitDefinitionConfig> splite;	
	
	
	private List<MergeDefinitionConfig> mergee;
	
	public void setMergee(List<MergeDefinitionConfig> mergee) {
		this.mergee = mergee;
	}
	
	public List<MergeDefinitionConfig> getMergee(){
		return this.mergee;
	}
	
	public void setSplite(Map<Integer, SplitDefinitionConfig> splite) {
		this.splite = splite;
	}
	
	public Map<Integer, SplitDefinitionConfig> getSplite(){
		return this.splite;
	}
	
	
	
    private SplitDefinition splitDefinition;
    
    private MergeDefinition mergeDefinition;
    
    private NormalizeDefinition normalizerDefinition;
    
       
    public PruneDefinition getPruneDefinition() {
    	return new PruneDefinition(this.columnsToPrune);
    }
    
    public void setColumnsToPrune(Set<Integer> columnsToPrune) {
    	this.columnsToPrune = columnsToPrune;
    }
    
    public Set<Integer> getColumnsToPrune(){
    	return this.columnsToPrune;
    }
    
    @Value("#{${split}}")
    public void setSplitDefinition(Map<Integer, String> columnsToSplit) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
    	this.splitDefinition = new SplitDefinition();
    	
    	for (final Entry<Integer, String> split : columnsToSplit.entrySet()) {
    		final int column = split.getKey();
    		final String splitString = split.getValue();
    		
    		final String[] splitParams = splitString.split(":", 3);
    		
    		if (splitParams != null && splitParams.length >= 2){
				final String className = splitParams[0];
				
				final String[] ctorParams = Arrays.copyOfRange(splitParams, 1, splitParams.length);
				final Object obj = ClassNameObjectConverter.getObject("dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldsplitter", className, ctorParams);
				final Splitter splitter = (Splitter) obj;
				
				this.splitDefinition.setSplitter(column, splitter);
			}
			else{
				throw new IllegalArgumentException();
			}
    	}		
    }
    
    public void setSplitDefinition(SplitDefinition splitDef) {
    	this.splitDefinition = splitDef;
    }
    
    @Value("#{${merge}}")
    public void setMergeDefinition(List<List<String>> columnsToMerge) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
    	this.mergeDefinition = new MergeDefinition();
    	
    	for (final List<String> merge : columnsToMerge) {
    		if (merge.size() < 3) {
    			throw new RuntimeException();
    		}
    		
    		final String mergerName = merge.get(merge.size()-1);
    		final Object obj = ClassNameObjectConverter.getObject("dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldmerger", mergerName);
			final Merger merger = (Merger) obj;
			
    		final Integer[] columns = new Integer[merge.size()-1];
    		
    		for (int i = 0; i < merge.size() - 1; i++) {
    			columns[i] = Integer.valueOf(merge.get(i));
    		}	
    		
    		this.mergeDefinition.setMerger(columns, merger);
    	}
    }
    
    public void setMergeDefinition(MergeDefinition mergeDef) {
    	this.mergeDefinition = mergeDef;
    }
    
    @Value("#{${normalize}}")
    public void setNormalizeDefinition(Map<Integer, String> columnsToNormalize) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
    	this.normalizerDefinition = new NormalizeDefinition();
    	
    	for (final Entry<Integer, String> norm : columnsToNormalize.entrySet()) {
    		final Integer column = norm.getKey();
    		final String normDefParam = norm.getValue();  	  		
			final String[] parts =normDefParam.split(":");
    			
			if (parts == null || parts.length < 1) {
				throw new RuntimeException();
			}
    			
			final String normalizerName = parts[0];
			final String[] ctorParams;
			if (parts.length == 2) {
				ctorParams = parts[1].split(",");
			}
			else if (parts.length > 2){
				throw new RuntimeException();
			}
			else {
				ctorParams = new String[0];
			}
						
			final Object obj = 
					ClassNameObjectConverter.getObject("dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer", normalizerName, ctorParams);
			
			final Normalizer normalizer = (Normalizer) obj;
			this.normalizerDefinition.setNormalizer(column, normalizer);
		}
    }

    public void setNormalizerDefinition(NormalizeDefinition normDef) {
    	this.normalizerDefinition = normDef;
    }
    
    public CSVInputFileConfig getCsvInputFileConfig () {
    	return this.csvInputFileConfig;
    }
    
    public void setCsvInputFileConfig(CSVInputFileConfig csvInputFileConfig) {
    	this.csvInputFileConfig = csvInputFileConfig;
    }
    
    public SplitDefinition getSplitDefinition() {
    	return this.splitDefinition;
    }
    
    public NormalizeDefinition getNormalizeDefinition() {
    	return this.normalizerDefinition;
    }
    
    public MergeDefinition getMergeDefinition() {
    	return this.mergeDefinition;
    }
   
}