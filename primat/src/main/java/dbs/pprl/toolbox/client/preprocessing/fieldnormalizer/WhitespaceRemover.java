package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;


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