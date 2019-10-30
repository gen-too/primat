package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer;


public final class NonDigitRemover extends RegexReplacer{

	private static final String REGEX = "[^0-9]";
	
	public NonDigitRemover() {
		super(REGEX, "");
	}
}