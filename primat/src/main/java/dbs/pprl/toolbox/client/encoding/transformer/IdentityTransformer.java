package dbs.pprl.toolbox.client.encoding.transformer;

import java.util.HashSet;
import java.util.Set;

import dbs.pprl.toolbox.client.encoding.attributes.Attribute;

public class IdentityTransformer extends Transformer{

	@Override
	public Set<String> transform(Attribute<?> attr) {
		final Set<String> result = new HashSet<>();
		final String value = attr.getStringValue();
		result.add(value);
		return result;
	}

}