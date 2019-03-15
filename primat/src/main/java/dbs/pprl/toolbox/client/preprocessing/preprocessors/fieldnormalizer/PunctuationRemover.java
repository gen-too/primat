package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer;


/**
 * Remove whitespace.
 * 
 * @author mfranke
 *
 */
public class PunctuationRemover extends RegexReplacer{

	private static final String REGEX = "\\p{Punct}";
	
	public PunctuationRemover() {
		super(REGEX, "");
	}
}