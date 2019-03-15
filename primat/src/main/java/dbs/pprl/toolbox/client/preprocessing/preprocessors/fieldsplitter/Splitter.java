package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldsplitter;

public interface Splitter {

	public String[] split(String string);

	public int parts();
	
}