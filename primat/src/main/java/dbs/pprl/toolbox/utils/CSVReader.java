package dbs.pprl.toolbox.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * 
 * @author mfranke
 *
 */
public class CSVReader {

	public static final CSVFormat CSV_DEFAULT_FORMAT = CSVFormat.RFC4180;
	
	private final CSVParser csvParser;
	private final LinkedList<String> header;
	
	public CSVReader(String filePath, boolean hasHeader) throws IOException{
		this(filePath, hasHeader, CSV_DEFAULT_FORMAT);
	}
	
	public CSVReader(String filePath, boolean hasHeader, char delimiter) throws IOException{
		this(filePath, hasHeader, CSV_DEFAULT_FORMAT.withDelimiter(delimiter));
	}
	
	public CSVReader(String filePath, boolean hasHeader, CSVFormat csvFormat) throws IOException{
		final Reader in = new FileReader(filePath);     
	    		
		if (hasHeader){
			this.csvParser = csvFormat.withFirstRecordAsHeader().parse(in);		
			final Map<String, Integer> header = csvParser.getHeaderMap();
			this.header = new LinkedList<String>(header.keySet());
		}
		else{
			this.csvParser = csvFormat.parse(in);		
			this.header = null;
		}
	}
	
	public static int numberOfColumns(List<CSVRecord> records) {
		if (records == null || records.size() == 0) {
			return -1;
		}
		else {
			return records.get(0).size();
		}
	}
	
	public static LinkedList<String> getDefaultHeader(List<CSVRecord> records) {
		if (records == null || records.size() == 0) {
			return null;
		}
		else {
			final CSVRecord record = records.get(0);
			final LinkedList<String> defaultHeader = new LinkedList<>();
			
			final int columns = record.size();
			for (int i = 0; i < columns; i++) {
				defaultHeader.add("Column_" + i);
			}
			
			return defaultHeader;
		}	
	}

	public LinkedList<String> getHeader(){
		return this.header;
	}
	
	public List<CSVRecord> read() throws IOException{   
		return this.csvParser.getRecords(); 
	}
}