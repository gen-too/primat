package dbs.pprl.toolbox.client.blocking;

import java.util.List;
import java.util.Map;

import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;

/**
 * 
 * @author mfranke
 * 
 */
public interface Blocker {
	
	public Map<CSVRecordEntry, List<String>> block(List<CSVRecordEntry> records);
}
