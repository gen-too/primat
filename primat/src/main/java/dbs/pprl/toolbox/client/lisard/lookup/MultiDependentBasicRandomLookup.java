package dbs.pprl.toolbox.client.lisard.lookup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class MultiDependentBasicRandomLookup extends BasicRandomLookup{

	private SimpleDependentBasicRandomLookup lookup;
	private List<SimpleDependentBasicRandomLookup> dependencyLookups;
	
		
	public MultiDependentBasicRandomLookup() {
		
	}
	
	public void setDependencies(List<String> dependencies) {
		if (dependencies == null || dependencies.isEmpty() || dependencies.size() != dependencyLookups.size()) {
			throw new RuntimeException();
		}
		
		for (int i = 0; i < dependencyLookups.size(); i++) {
			final SimpleDependentBasicRandomLookup l = dependencyLookups.get(i);
			final String dep = dependencies.get(i);
			l.setDependency(dep);
		}
	}
	
	@Override
	protected String getRandomValue(Random rnd) {
		final Set<String> possibleValues = this.getPossibleValues();
		
		String value = null;
		
		while (value == null && !possibleValues.contains(value)) {
			value = lookup.getRandomValue(rnd);
		}
		
		return value;
	}

	private Set<String> getPossibleValues(){
		final Set<String> possibleValues = new HashSet<String>();
		
		for (final SimpleDependentBasicRandomLookup l : this.dependencyLookups) {
			final List<String> values = l.getLookupValues();
			
			if (possibleValues.isEmpty()) {
				possibleValues.addAll(values);
			}
			else {
				possibleValues.retainAll(values);
			}
		}
		return possibleValues;
	}
	
	@Override
	public List<String> getLookupValues() {
		return new ArrayList<String>(this.getPossibleValues());
	}
}