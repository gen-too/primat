package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;

import java.util.Map;

public abstract class StringMapper implements Normalizer{
	
	private final Map<String, String> substringReplacement;
	
	public StringMapper(Map<String, String> substringReplacement) {
		this.substringReplacement = substringReplacement;
	}
	
	@Override
	public String normalize(String string) {
		for (final String key : substringReplacement.keySet()){
			if (string.toLowerCase().contains(key.toLowerCase())){
				final String replacement = substringReplacement.get(key);
				return replacement;
			}
		}
		return string;
	}
}