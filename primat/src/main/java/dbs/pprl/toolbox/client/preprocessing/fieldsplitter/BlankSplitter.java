package dbs.pprl.toolbox.client.preprocessing.fieldsplitter;

public class BlankSplitter extends RegexSplitter{
	
	public static final String REGEX = " ";

	public BlankSplitter(int parts) {
		super(REGEX, parts);
	}
}