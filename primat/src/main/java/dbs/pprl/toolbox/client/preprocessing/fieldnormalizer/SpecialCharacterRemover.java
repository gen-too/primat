package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;


/**
 * Remove any special character.
 * @author mfranke
 *
 */
public final class SpecialCharacterRemover extends RegexReplacer{

	private static final String REGEX = "\\W";
	
	public SpecialCharacterRemover() {
		super(REGEX, "");
	}
}