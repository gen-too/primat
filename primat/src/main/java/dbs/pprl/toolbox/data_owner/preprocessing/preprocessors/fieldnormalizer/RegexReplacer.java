package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer;


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
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}