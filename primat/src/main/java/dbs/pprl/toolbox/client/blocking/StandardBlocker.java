package dbs.pprl.toolbox.client.blocking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;

public class StandardBlocker implements Blocker{

//	private BlockingKeyDefinition[] blockerDef;
	private Iterable<BlockingKeyDefinition> blockerDef;
	
	public StandardBlocker( Iterable<BlockingKeyDefinition> blockerDef) {
		this.blockerDef = blockerDef;
	}

	@Override
	public Map<CSVRecordEntry, List<String>> block(List<CSVRecordEntry> records) {
		final Map<CSVRecordEntry, List<String>> result = new HashMap<>(records.size());
		
		for (final CSVRecordEntry rec : records){
			final List<String> bks = new ArrayList<>();
			
			for (final BlockingKeyDefinition bkDef : this.blockerDef){
				final String bk = bkDef.getBlockingKey(rec);
				bks.add(bk);
			}
			
			result.put(rec, bks);
		}
		
		return result;
	}
	
}
