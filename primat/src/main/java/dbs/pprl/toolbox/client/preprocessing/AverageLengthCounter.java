package dbs.pprl.toolbox.client.preprocessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;
import dbs.pprl.toolbox.utils.CSVWriter;

// TODO: Rework
public class AverageLengthCounter {

	public static final String TRANSFORMATION_SHORTCUT = "fieldLength";
	
	private final String pathToFile;
	private final boolean hasHeader;
	private final int idColumn;
	
	public AverageLengthCounter(String pathToFile, boolean hasHeader, int idColumn){
		this.pathToFile = pathToFile;
		this.hasHeader = hasHeader;
		this.idColumn = idColumn;
	}
	
	private Map<Integer, DescriptiveStatistics> countLength(List<CSVRecordEntry> records){
		final Map<Integer, DescriptiveStatistics> statMap = new LinkedHashMap<>();
		
		// TODO: NullPointerException if hasHeader = false
		if (this.hasHeader){
			for (Integer key : this.header.values()){
				statMap.put(key, new DescriptiveStatistics());
			}
		}
		
		for (final CSVRecordEntry rec : records){
			final List<String> attributes = rec.getAttributes();
			final ListIterator<String> it = attributes.listIterator();
			
			while(it.hasNext()){
				final int index = it.nextIndex();
				final String attr = it.next();
				final int valueLength = attr.replaceAll(" ", "").length();
				it.set(String.valueOf(valueLength));
				statMap.get(index).addValue(valueLength);
			}
		}
		return statMap;
	}
	
	public List<Double> count() throws IOException{
		final List<CSVRecordEntry> records = this.readAndParseFile();
		final Map<Integer, DescriptiveStatistics> statMap = countLength(records);
		
		final List<Double> avgLengths = new ArrayList<>();
		for (final DescriptiveStatistics stat : statMap.values()){
			avgLengths.add(stat.getMean());
		}
		
		return avgLengths;
	}
	
	public void writeLength(List<Double> avgLengths) throws IOException{
		final CSVWriter csvWriter = new CSVWriter(this.getOutputPath());
		if (hasHeader){
			csvWriter.writeObjectWithHeader(avgLengths, header.keySet());
		}
		else{
			csvWriter.writeObject(avgLengths);
		}
	}
	
	public void countAndWrite() throws IOException{		
		final List<Double> avgLengths = this.count();
		this.writeLength(avgLengths);
	}	
	
	@Override
	protected String opName() {
		return TRANSFORMATION_SHORTCUT;
	}
	
	public static void main(String[] args) throws IOException{
		final String file = "/home/mfranke/Schreibtisch/N1.csv";
		
		final boolean hasHeader = true;
		
		final AverageLengthCounter lengthCounter = new AverageLengthCounter(file, hasHeader);
		lengthCounter.countAndWrite();
	}
}