package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldsplitter;

public class PunctuationSplitter extends RegexSplitter{
	
	public static final String REGEX = "\\p{Punct}";

	public PunctuationSplitter(int parts) {
		super(REGEX, parts);
	}
}