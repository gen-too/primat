package dbs.pprl.toolbox.client.lisard.lookup;

import java.util.List;
import java.util.Random;

public class SimpleRandomLookup extends IndipendentBasicRandomLookup {

	protected final List<String> values;
		
	public SimpleRandomLookup(List<String> values) {
		this.values = values;
	}

	@Override
	protected String getRandomValue(Random rnd) {
		final int index = rnd.nextInt(values.size());
		return this.values.get(index);
	}

	@Override
	public List<String> getLookupValues() {
		return this.values;
	}
}