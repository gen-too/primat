package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;


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