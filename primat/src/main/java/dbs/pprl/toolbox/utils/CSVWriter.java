package dbs.pprl.toolbox.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import dbs.pprl.toolbox.client.data.Printable;

public class CSVWriter {

	public final CSVFormat CSV_FORMAT = CSVFormat.RFC4180;
	
	private final CSVPrinter csvPrinter;
	
	public CSVWriter(String outputFile) throws IOException{
		final FileWriter fileWriter;
		
		if (!outputFile.endsWith(".csv")) {
			fileWriter = new FileWriter(outputFile + ".csv");
		}
		else {
			fileWriter = new FileWriter(outputFile);
		}
		
		this.csvPrinter = new CSVPrinter(fileWriter, CSV_FORMAT);
	}
	
	public void close() throws IOException{
		this.csvPrinter.flush();
		this.csvPrinter.close();
	}

	
	public void writeRecords(List<? extends Printable> records) throws IOException{		
		for (final Printable entry : records){
			this.csvPrinter.printRecord(entry.getPrint());
		}
		
		this.close();
	}
	
	public void writeRecords(List<? extends Printable> records, Iterable<?> header) throws IOException{
		this.csvPrinter.printRecord(header);
		this.writeRecords(records);
	}
}