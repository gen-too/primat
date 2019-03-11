package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;


/**
 * Remove any special character.
 * @author mfranke
 *
 */
public class RegexReplacer implements Normalizer{

	private final String regex;
	private final String replacement;
	
	public RegexReplacer(String regex, String replacement) {
		this.regex = regex;
		this.replacement = replacement;
	}
	
	@Override
	public String normalize(String string) {
		return string.replaceAll(regex, replacement);
	}

}