package dbs.pprl.toolbox.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

public class CSVWriter {

	public final CSVFormat CSV_FORMAT = CSVFormat.RFC4180;
	
	private final CSVPrinter csvPrinter;
	
	public CSVWriter(String outputFile) throws IOException{
		final FileWriter fileWriter = new FileWriter(outputFile);
		this.csvPrinter = new CSVPrinter(fileWriter, CSV_FORMAT);
	}
	
	public void close() throws IOException{
		this.csvPrinter.flush();
		this.csvPrinter.close();
	}

	
	public void writeEncodedRecords(List<EncodedRecord> values) throws IOException{
		for (final EncodedRecord rec : values){
			final String id = rec.getId();
			final String bitVector = BitSetUtils.toBase64(rec.getBitVector());
			this.csvPrinter.printRecord(id, bitVector);
		}
		this.close();
	}
	
	public void writeObjectWithHeader(Iterable<?> record, Iterable<?> header) throws IOException{
		this.csvPrinter.printRecord(header);
		this.writeObject(record);
	}
	
	public void writeObject(Iterable<?> record) throws IOException{
		this.csvPrinter.printRecord(record);
		this.close();
	}
	
	public void writeCSVRecordsWithHeader(List<CSVRecordEntry> records, Iterable<?> header) throws IOException{
		this.csvPrinter.printRecord(header);
		this.writeCSVRecords(records);
	}
	
	public void writeCSVRecords(List<CSVRecordEntry> records) throws IOException{
		for (CSVRecordEntry entry : records){
			this.csvPrinter.printRecord(entry.getAttributes());
		}
		this.close();
	}
}