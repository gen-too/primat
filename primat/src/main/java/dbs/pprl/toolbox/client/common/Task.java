package dbs.pprl.toolbox.client.common;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import dbs.pprl.toolbox.client.common.config.CSVInputFileConfig;
import dbs.pprl.toolbox.client.data.CSVRecordWrapper;
import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.data.records.EncodedRecord;
import dbs.pprl.toolbox.utils.CSVReader;
import dbs.pprl.toolbox.utils.CSVWriter;

public abstract class Task {

	protected final CSVInputFileConfig csvInputFile;
	protected LinkedList<String> inputFileHeader;
	
	/**
	 * 
	 * @param pathToFile Path to csv record file.
	 * @param hasHeader boolean value to indicate if csv-file contains a header, e. g., schema information
	 * @param idColumn specifies to column where the record identifier is placed
	 */
	public Task(CSVInputFileConfig csvInputFile) {
		this.csvInputFile = csvInputFile;
		this.inputFileHeader = null;
	}
	
	
	protected List<Record> readFile() throws IOException, AttributeParseException{
		final String pathToFile = this.csvInputFile.getFile();
		final boolean hasHeader = this.csvInputFile.getHeader();
		
		final CSVReader csvReader = new CSVReader(pathToFile, hasHeader);
		this.inputFileHeader = csvReader.getHeader();
		final List<CSVRecord> csvRecords = csvReader.read();

		final CSVRecordWrapper wrapper = new CSVRecordWrapper(this.csvInputFile.getColumnType());
		return wrapper.from(csvRecords);
	}

	
	protected void writeFile(List<Record> records) throws IOException{
		final CSVWriter csvWriter = new CSVWriter(this.getOutputPath(this.getTaskName()));
		if (this.inputFileHeader != null && !this.inputFileHeader.isEmpty()){
			csvWriter.writeRecords(records, this.inputFileHeader);
		}
		else{
			csvWriter.writeRecords(records);
		}
	}
	
	protected void writeEncodedFile(List<EncodedRecord> records) throws IOException{
		final CSVWriter csvWriter = new CSVWriter(this.getOutputPath(this.getTaskName()));
		csvWriter.writeRecords(records);
	}
	
	protected String getOutputPath(String taskName){
		final String[] pathComponents = this.csvInputFile.getFile().split("\\.");
		StringBuilder builder = new StringBuilder();
		builder.append(pathComponents[0]);
		builder.append("_");
		builder.append(taskName);
		return builder.toString();
	}
	
	public abstract void execute() throws RuntimeException, IOException, AttributeParseException;
	
	public abstract String getTaskName();
}
