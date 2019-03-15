package dbs.pprl.toolbox.client.encoding.extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dbs.pprl.toolbox.client.data.attributes.Attribute;


public class FeatureExtractorChain extends FeatureExtractor{

	private List<FeatureExtractor> featureExtractors;
	
	public FeatureExtractorChain() {
		this.featureExtractors = new ArrayList<>();
	}
	
	@SafeVarargs
	public FeatureExtractorChain(FeatureExtractor... mappingFunctions) {
		this.featureExtractors = Arrays.asList(mappingFunctions);
	}
	
	public void add(FeatureExtractor func){
		this.featureExtractors.add(func);
	}

	@Override
	public LinkedHashSet<String> extract(Attribute<?> attr) {
		final LinkedHashSet<String> result = new LinkedHashSet<>();
		
		for (final FeatureExtractor mapFunction : this.featureExtractors){
			final Set<String> funcResult = mapFunction.extract(attr);
			result.addAll(funcResult);
		}
		
		return result;
	}

}