package dbs.pprl.toolbox.client.lisard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.lisard.lookup.FrequencyLookup;

/**
 * 
 * @author mfranke
 *
 */
public class Corruptor {

	private double corruptionRate;
	
	private FrequencyLookup errorRatePerRecord;
	 
	
	public void corrupt(List<Record> input, long seed){	
		final int recsToCorrupt = (int) (this.corruptionRate * input.size());
		
		final List<Record> corruptedRecords = new ArrayList<>(recsToCorrupt);
		
		final Random rnd = new Random(seed);
		
		final int[] recPos = 
				 rnd.ints(0, input.size())
					.distinct()
					.limit(recsToCorrupt)
					.toArray();
		
		for (final int pos : recPos) {
			corruptedRecords.add(input.get(pos));
		}
		
		for (final Record rec : corruptedRecords) {
			this.errorRatePerRecord.setSeed(rec.getId().hashCode());
			final int errors = Integer.valueOf(this.errorRatePerRecord.getValue());
			for (int i = 0; i < errors; i++) {
				
			}
		}
	}
	
}
