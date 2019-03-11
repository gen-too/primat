package dbs.pprl.toolbox.lu.matching;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import dbs.pprl.toolbox.utils.CSVToEncodedRecordTransformer;
import dbs.pprl.toolbox.utils.CSVReader;

/**
 * 
 * @author mfranke
 *
 */
public class Matching {

	public static List<EncodedRecord> readRecordsFromFile(String path) throws IOException{
		final CSVReader csvReader = new CSVReader(path, false);
		final List<CSVRecord> stringRecords = csvReader.read();
		final CSVToEncodedRecordTransformer csvPojoTransformer = new CSVToEncodedRecordTransformer();
		return csvPojoTransformer.transform(stringRecords);
	}
	
	public static void main(String[] args) throws Exception{
		final ConfigurationLoader configLoader = new ConfigurationLoader();
		
		final Matcher matcher = configLoader.getMatcher();
		final String pathA = configLoader.getFileA();
		final String pathB = configLoader.getFileB();
		
		final List<EncodedRecord> encodedRecordsA = readRecordsFromFile(pathA);	
		System.out.println("Read records party A done. #Records: " + encodedRecordsA.size());
		
		final List<EncodedRecord> encodedRecordsB = readRecordsFromFile(pathB);
		System.out.println("Read records party B done. #Records: " + encodedRecordsB.size());
		
		matcher.match(encodedRecordsA, encodedRecordsB);
		
//		final Map<String, Number> metrics = matcher.getMetrics();
//		System.out.println(metrics);
	}	
}