package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldmerger;

public class DotMerger extends DefaultMerger{

	public static final String SEPARATOR = ".";
	
	public DotMerger() {
		super(SEPARATOR);
	}
}