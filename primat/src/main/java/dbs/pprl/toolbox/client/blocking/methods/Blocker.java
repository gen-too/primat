package dbs.pprl.toolbox.client.blocking.methods;

import java.util.List;
import java.util.Map;

import dbs.pprl.toolbox.client.data.records.Record;

/**
 * 
 * @author mfranke
 * 
 */
public interface Blocker {
	
	public Map<Record, List<String>> block(List<Record> records);
}
