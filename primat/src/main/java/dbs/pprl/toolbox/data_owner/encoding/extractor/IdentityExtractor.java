package dbs.pprl.toolbox.data_owner.encoding.extractor;

import java.util.LinkedHashSet;

import dbs.pprl.toolbox.data_owner.data.attributes.Attribute;

public class IdentityExtractor extends FeatureExtractor{

	@Override
	public LinkedHashSet<String> extract(Attribute<?> attr) {
		final LinkedHashSet<String> result = new LinkedHashSet<>();
		final String value = attr.getStringValue();
		result.add(value);
		return result;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}