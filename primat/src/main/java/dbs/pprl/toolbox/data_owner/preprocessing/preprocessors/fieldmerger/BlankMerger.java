package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldmerger;

public class BlankMerger extends DefaultMerger{

	public static final String SEPARATOR = " ";
	
	public BlankMerger() {
		super(SEPARATOR);
	}
}