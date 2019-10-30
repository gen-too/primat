package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer;


/**
 * Remove any number.
 * 
 * @author mfranke
 *
 */
public final class DigitRemover extends RegexReplacer{

	private static final String REGEX = "[0-9]";
	
	public DigitRemover() {
		super(REGEX, "");
	}
}