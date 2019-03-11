package dbs.pprl.toolbox.client.encoding.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dbs.pprl.toolbox.client.encoding.attributes.Attribute;


public class TransformerChain extends Transformer{

	private List<Transformer> transformers;
	
	public TransformerChain() {
		this.transformers = new ArrayList<>();
	}
	
	@SafeVarargs
	public TransformerChain(Transformer... mappingFunctions) {
		this.transformers = Arrays.asList(mappingFunctions);
	}
	
	public void add(Transformer func){
		this.transformers.add(func);
	}

	@Override
	public Set<String> transform(Attribute<?> attr) {
		final Set<String> result = new HashSet<>();
		
		for (final Transformer mapFunction : this.transformers){
			final Set<String> funcResult = mapFunction.transform(attr);
			result.addAll(funcResult);
		}
		
		return result;
	}

}