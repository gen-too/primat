package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldsplitter;

public interface Splitter {

	public String[] split(String string);

	public int parts();
	
}