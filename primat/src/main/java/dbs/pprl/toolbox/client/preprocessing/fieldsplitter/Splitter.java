package dbs.pprl.toolbox.client.preprocessing.fieldsplitter;

public interface Splitter {

	public String[] split(String string);

	public int parts();
	
}