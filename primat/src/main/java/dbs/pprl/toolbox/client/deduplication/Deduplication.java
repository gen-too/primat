package dbs.pprl.toolbox.client.deduplication;

import java.io.IOException;
import java.util.List;

import dbs.pprl.toolbox.client.common.Task;
import dbs.pprl.toolbox.client.common.config.CSVInputFileConfig;
import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.records.Record;

public class Deduplication extends Task{

	public Deduplication(CSVInputFileConfig csvInputFileConfig) {
		super(csvInputFileConfig);
	}

	public static final String TASK = "dedup";
	
	@Override
	public void execute() throws RuntimeException, IOException, AttributeParseException {
		final List<Record> records = this.readFile();
		
		// Blocking
		
		// String similarity measures
		
		// Classification
		
		System.out.println(records);
	}

	@Override
	public String getTaskName() {
		return TASK;
	}
 
}
