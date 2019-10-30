package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldsplitter;

public class CommaSplitter extends RegexSplitter{
	
	public static final String REGEX = ",";

	public CommaSplitter(int parts) {
		super(REGEX, parts);
	}
}