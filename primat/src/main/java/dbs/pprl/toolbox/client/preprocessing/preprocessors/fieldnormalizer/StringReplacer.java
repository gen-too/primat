package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer;

import java.util.Map;


public abstract class StringReplacer implements Normalizer{
	
	private final Map<String, String> stringReplacementMap;
	
	public StringReplacer(Map<String, String> stringReplacementMap) {
		this.stringReplacementMap = stringReplacementMap;
	}
	
	@Override
	public String normalize(String string) {
		String result = string;
		for (final String key : stringReplacementMap.keySet()){
			final String replacement = stringReplacementMap.get(key);
			result = result.replace(key, replacement);
		}
		return result;
	}

}