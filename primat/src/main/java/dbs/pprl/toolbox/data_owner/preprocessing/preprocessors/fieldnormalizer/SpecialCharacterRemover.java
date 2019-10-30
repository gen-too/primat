package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer;


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