package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NormalizeDefinition {
		
	private String name;
	
	private Map<Integer, Normalizer> columnNormalizerMapping;
	
	public NormalizeDefinition() {
		this("");
	}
	
	public NormalizeDefinition(String name) {
		this.name = name;
		this.columnNormalizerMapping = new HashMap<>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Collection<Normalizer> getAllNormalizer() {
		return this.columnNormalizerMapping.values();
	}
	
	public Set<Integer> getAffectedColumns() {
		return this.columnNormalizerMapping.keySet();
	}

	public Normalizer setNormalizer(int columnToNormalize, Normalizer normalizer){
		return this.columnNormalizerMapping.put(columnToNormalize, normalizer);
	}
	
	public static NormalizeDefinition from(SimpleNormalizeDefinition simpleDef) {
		final NormalizeDefinition normDef = new NormalizeDefinition(simpleDef.getName());
		final Set<Integer> columns = simpleDef.getColumns();
		final List<Normalizer> normalizer = simpleDef.getNormalizer();
		
		for (final Integer col : columns) {
			final NormalizerChain normChain = new NormalizerChain(normalizer);
			normDef.setNormalizer(col, normChain);
		}
		return normDef;
	}
	
	
	public Normalizer getNormalizer(int columnToNormalize){
		return this.columnNormalizerMapping.get(columnToNormalize);
	}
	
	public Map<Integer, Normalizer> getColumnToNormalizerMapping(){
		return this.columnNormalizerMapping;
	}
	
	public void merge(NormalizeDefinition normDef) {
		final Map<Integer,Normalizer> normMapping = normDef.getColumnToNormalizerMapping();
		
		normMapping.forEach( (columnId, normalizer) -> {
			if (this.columnNormalizerMapping.containsKey(columnId)) {
				final Normalizer currentNormalizer = this.columnNormalizerMapping.get(columnId);
				if (currentNormalizer instanceof NormalizerChain) {
					final NormalizerChain currentNormChain = (NormalizerChain) currentNormalizer;
					currentNormChain.add(normalizer);
				}
				else {
					final NormalizerChain normChain = new NormalizerChain();
					normChain.add(currentNormalizer);
					normChain.add(normalizer);
				}
			}
			else {
				this.columnNormalizerMapping.put(columnId, normalizer);
			}
		});
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}