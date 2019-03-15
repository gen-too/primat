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

	public static final CSVFormat CSV_FORMAT = CSVFormat.RFC4180;
	
	private final CSVParser csvParser;
	private final LinkedList<String> header;
	
	public CSVReader(String filePath, boolean hasHeader) throws IOException{
		final Reader in = new FileReader(filePath);     
	    		
		if (hasHeader){
			this.csvParser = CSV_FORMAT.withFirstRecordAsHeader().parse(in);		
			final Map<String, Integer> header = csvParser.getHeaderMap();
			this.header = new LinkedList<String>(header.keySet());
		}
		else{
			this.csvParser = CSV_FORMAT.parse(in);		
			this.header = null;
		}
	}
	
	public LinkedList<String> getHeader(){
		return this.header;
	}
	
	public List<CSVRecord> read() throws IOException{   
		return this.csvParser.getRecords(); 
	}
}