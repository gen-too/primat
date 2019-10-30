package dbs.pprl.toolbox.data_owner.blocking.methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbs.pprl.toolbox.data_owner.data.records.Record;

public class StandardBlocker implements Blocker{

	private Iterable<BlockingKey> blockerDef;
	
	public StandardBlocker(Iterable<BlockingKey> blockerDef) {
		this.blockerDef = blockerDef;
	}

	@Override
	public Map<Record, List<String>> block(List<Record> records) {
		final Map<Record, List<String>> result = new HashMap<>(records.size());
		
		for (final Record rec : records){
			final List<String> bks = new ArrayList<>();
			
			for (final BlockingKey bkDef : this.blockerDef){
				final String bk = bkDef.getBlockingKeyValue(rec);
				bks.add(bk);
			}
			
			result.put(rec, bks);
		}
		
		return result;
	}
}