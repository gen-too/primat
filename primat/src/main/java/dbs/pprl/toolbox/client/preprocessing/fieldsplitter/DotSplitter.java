package dbs.pprl.toolbox.client.preprocessing.fieldsplitter;

public class DotSplitter extends RegexSplitter{
	
	public static final String REGEX = "\\.";

	public DotSplitter(int parts) {
		super(REGEX, parts);
	}
}