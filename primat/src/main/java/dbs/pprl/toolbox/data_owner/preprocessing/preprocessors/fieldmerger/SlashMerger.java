package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldmerger;

public class SlashMerger extends DefaultMerger{

	public static final String SEPARATOR = "/";
	
	public SlashMerger() {
		super(SEPARATOR);
	}
}