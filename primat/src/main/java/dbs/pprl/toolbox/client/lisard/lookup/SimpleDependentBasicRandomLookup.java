package dbs.pprl.toolbox.client.lisard.lookup;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class SimpleDependentBasicRandomLookup extends DependentBasicRandomLookup {
	
	protected Map<String, IndipendentBasicRandomLookup> lookup;
	protected String dependency;
		
	public SimpleDependentBasicRandomLookup(Map<String, IndipendentBasicRandomLookup> lookup) {
		super();
		this.lookup = lookup;
		this.dependency = null;
	}
	
	@Override
	public String setDependency(String value) {
		final String oldValue = this.dependency;
		this.dependency = value;
		return oldValue;
	}

	@Override
	protected String getRandomValue(Random rnd) {
		if (this.dependency == null || this.dependency.isEmpty()) {
			throw new RuntimeException();
			// Maybe ok? (Use complete lookup instead of relevant part)
		}
		else {
			if (!lookup.containsKey(this.dependency)) {
				throw new RuntimeException();
			}
			else {
				final IndipendentBasicRandomLookup indiBasicRandomLookup = this.lookup.get(this.dependency);
				return indiBasicRandomLookup.getRandomValue(rnd);
			}
		}
	}

	@Override
	public List<String> getLookupValues() {
		if (this.dependency == null || this.dependency.isEmpty()) {
			throw new RuntimeException();
			// Maybe ok? (Use complete lookup instead of relevant part)
		}
		else {
			if (!lookup.containsKey(this.dependency)) {
				throw new RuntimeException();
			}
			else {
				final IndipendentBasicRandomLookup indiBasicRandomLookup = this.lookup.get(this.dependency);
				return indiBasicRandomLookup.getLookupValues();
			}
		}
	}			
}