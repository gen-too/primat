package dbs.pprl.toolbox.client.encoding.extractor;

import java.util.Arrays;
import java.util.LinkedHashSet;

import dbs.pprl.toolbox.client.data.attributes.Attribute;

public class SubstringByRegexExtractor extends FeatureExtractor{

	private String regex;
	
	public SubstringByRegexExtractor(String regex) {
		this.regex = regex;
	}
	
	@Override
	public LinkedHashSet<String> extract(Attribute<?> attr) {
		final String attrValue = attr.getStringValue();
		final String[] parts = attrValue.split(this.regex);
		final LinkedHashSet<String> result = new LinkedHashSet<>(Arrays.asList(parts));
		return result;
	}	
}