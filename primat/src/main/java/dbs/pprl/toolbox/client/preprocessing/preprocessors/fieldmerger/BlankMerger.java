package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldmerger;

public class BlankMerger extends DefaultMerger{

	public static final String SEPARATOR = " ";
	
	public BlankMerger() {
		super(SEPARATOR);
	}
}