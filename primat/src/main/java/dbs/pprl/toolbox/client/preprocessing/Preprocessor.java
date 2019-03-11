package dbs.pprl.toolbox.client.preprocessing;

import java.util.LinkedList;
import java.util.List;


import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;

public interface Preprocessor {
	
	public void apply(List<CSVRecordEntry> records, LinkedList<String> header);

}