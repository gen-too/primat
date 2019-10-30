package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldmerger;

import java.util.StringJoiner;

public class DefaultMerger implements Merger{

	private final String separator;
	
	public DefaultMerger(String separator){
		this.separator = separator;
	}
	
	@Override
	public String merge(String[] string) {
		final StringJoiner joiner = new StringJoiner(separator);
		for (final String s : string){
			joiner.add(s);
		}
		return joiner.toString();
	}	
}