package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer;


/**
 * Remove whitespace.
 * 
 * @author mfranke
 *
 */
public class WhitespaceRemover extends RegexReplacer{

	private static final String REGEX = "\\s+";
	
	public WhitespaceRemover() {
		super(REGEX, "");
	}
}