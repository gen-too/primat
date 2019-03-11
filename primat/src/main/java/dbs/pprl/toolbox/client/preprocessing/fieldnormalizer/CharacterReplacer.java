package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;

import java.util.Map;


public abstract class CharacterReplacer implements Normalizer{
	
	private final Map<Character, String> characterReplacementMap;
	
	public CharacterReplacer(Map<Character, String> characterReplacementMap) {
		this.characterReplacementMap = characterReplacementMap;
	}
	
	@Override
	public String normalize(String string) {
		final StringBuilder result = new StringBuilder();
		
		for (int i = 0; i < string.length(); i++){
			final char charAtPos = string.charAt(i);
			
			if (characterReplacementMap.containsKey(charAtPos)){
				final String charReplacement = characterReplacementMap.get(charAtPos);
				result.append(charReplacement);
			}
			else{
				result.append(charAtPos);
			}
		}
		
		return result.toString();
	}

}