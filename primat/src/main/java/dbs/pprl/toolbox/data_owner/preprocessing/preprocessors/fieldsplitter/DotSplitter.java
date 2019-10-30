package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldsplitter;

public class DotSplitter extends RegexSplitter{
	
	public static final String REGEX = "\\.";

	public DotSplitter(int parts) {
		super(REGEX, parts);
	}
}