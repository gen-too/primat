package dbs.pprl.toolbox.data_owner.lisard.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVRecord;

import dbs.pprl.toolbox.data_owner.lisard.lookup.FrequencyLookup;
import dbs.pprl.toolbox.data_owner.lisard.lookup.IndipendentBasicRandomLookup;
import dbs.pprl.toolbox.data_owner.lisard.lookup.SimpleDependentBasicRandomLookup;
import dbs.pprl.toolbox.data_owner.lisard.lookup.SimpleRandomLookup;
import dbs.pprl.toolbox.data_owner.lisard.lookup.FrequencyLookup.FrequencyLookupBuilder;
import dbs.pprl.toolbox.utils.CSVReader;

public class CsvInputHandler {

	public static FrequencyLookup readFrequencyLookup(String pathToFile, boolean hasHeader) throws IOException{		
		final List<CSVRecord> csvRecords = getCsvContent(pathToFile, hasHeader);
		
		final List<String> values = new ArrayList<String>(csvRecords.size());
		final List<Double> frequencies = new ArrayList<Double>(csvRecords.size());
		
		for (final CSVRecord rec : csvRecords) {
			final String value = rec.get(0);
			final Double frequency = Double.parseDouble(rec.get(1));
			
			values.add(value);
			frequencies.add(frequency);
		}
		
		final FrequencyLookup dic = new FrequencyLookupBuilder(values, frequencies).build();
		return dic;
	}
	
	public static SimpleDependentBasicRandomLookup readSimpleDependentBasicRandomLookup(String pathToFile, boolean hasHeader, boolean withFrequencies) throws IOException{		
		final List<CSVRecord> csvRecords = getCsvContent(pathToFile, hasHeader);
				
		return withFrequencies ? readFrequencyDependentLookup(csvRecords) : readDependentLookup(csvRecords);
	}	
	
	
	private static SimpleDependentBasicRandomLookup readFrequencyDependentLookup(List<CSVRecord> csvRecords) {
		final Map<String, Map<String, Double>> lookup = new HashMap<>(csvRecords.size());
		
		for (final CSVRecord rec : csvRecords){
			final String key = rec.get(0);
			final String value = rec.get(1);
			final Double freq = Double.parseDouble(rec.get(2));
			
			if (!lookup.containsKey(key)) {
				lookup.put(key, new HashMap<>());
			}
			
			lookup.get(key).put(value, freq);	
		}
		
		final Map<String, IndipendentBasicRandomLookup> lookupMap = new HashMap<>(lookup.size());
		
		for (final Entry<String, Map<String, Double>> entry : lookup.entrySet()) {
			final String dependency = entry.getKey();
			final Map<String, Double> valueProbMap = entry.getValue();
			final IndipendentBasicRandomLookup brl = new FrequencyLookupBuilder(valueProbMap).build();
			lookupMap.put(dependency, brl);
		}

		return new SimpleDependentBasicRandomLookup(lookupMap);	
	}
	
	private static SimpleDependentBasicRandomLookup readDependentLookup(List<CSVRecord> csvRecords) {
		final Map<String, List<String>> lookup = new HashMap<>(csvRecords.size());
		
		for (final CSVRecord rec : csvRecords){
			final String key = rec.get(0);
			final String value = rec.get(1);
			
			if (!lookup.containsKey(key)) {
				lookup.put(key, new ArrayList<>());
			}
			
			lookup.get(key).add(value);
		}
		
		final Map<String, IndipendentBasicRandomLookup> lookupMap = new HashMap<>(lookup.size());
		
		for (final Entry<String, List<String>> entry : lookup.entrySet()) {
			final String key = entry.getKey();
			final List<String> values = entry.getValue();
			final IndipendentBasicRandomLookup srl = new SimpleRandomLookup(values);
			lookupMap.put(key, srl);
		}
		
		return new SimpleDependentBasicRandomLookup(lookupMap);	
	}	
		
	//TODO Implement Exception
	private static List<CSVRecord> getCsvContent(String pathToFile, boolean hasHeader) throws IOException{
		final CSVReader csvReader = new CSVReader(pathToFile, hasHeader);
		
		final List<CSVRecord> csvRecords = csvReader.read();
		if (csvRecords == null || csvRecords.size() < 1){
			throw new RuntimeException("Error reading records!");
		}
		return csvRecords;
	}	
}