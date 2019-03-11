package dbs.pprl.toolbox.client.encoding.transformer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import dbs.pprl.toolbox.client.encoding.attributes.Attribute;

public class SplitTransformer extends Transformer{

	private String regex;
	
	public SplitTransformer(String regex) {
		this.regex = regex;
	}
	
	@Override
	public Set<String> transform(Attribute<?> attr) {
		final String attrValue = attr.getStringValue();
		final String[] parts = attrValue.split(this.regex);
		final Set<String> result = new HashSet<>(Arrays.asList(parts));
		return result;
	}	
}