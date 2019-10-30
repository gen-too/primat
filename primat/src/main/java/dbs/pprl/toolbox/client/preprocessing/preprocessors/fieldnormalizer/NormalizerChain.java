package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * Build a ordered chain of normalizer functions.
 * @author mfranke
 *
 */
public class NormalizerChain implements Normalizer{

	private List<Normalizer> normalizer;
	
	public NormalizerChain() {
		this.normalizer = new ArrayList<>();
	}
	
	@SafeVarargs
	public NormalizerChain(Normalizer... normalizer) {
		this.normalizer = Arrays.asList(normalizer);
	}
	
	public NormalizerChain(Collection<Normalizer> normalizer) {
		this.normalizer = new ArrayList<>(normalizer);
	}
	

	public void add(Normalizer normalizer){
		this.normalizer.add(normalizer);
	}
	
	public void addAll(Collection<Normalizer> normalizer){
		this.normalizer.addAll(normalizer);
	}

	public List<Normalizer> getNormalizer(){
		return this.normalizer;
	}
	
	@Override
	public String normalize(String string) {
		String result = string;
		for (final Normalizer normalizer : this.normalizer){
			result = normalizer.normalize(result);
		}
		return result;
	}	
}