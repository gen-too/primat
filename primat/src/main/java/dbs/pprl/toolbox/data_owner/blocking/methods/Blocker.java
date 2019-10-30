package dbs.pprl.toolbox.data_owner.blocking.methods;

import java.util.List;
import java.util.Map;

import dbs.pprl.toolbox.data_owner.data.records.Record;

/**
 * 
 * @author mfranke
 * 
 */
public interface Blocker {
	
	public Map<Record, List<String>> block(List<Record> records);
}
