package dbs.pprl.toolbox.client.preprocessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;
import dbs.pprl.toolbox.lu.blocking.Side;
import dbs.pprl.toolbox.utils.CSVWriter;

//TODO: REWORK, it is a util-class
public class GeneratedDatasetSplitter{

	public static final String ID_SUFFIX_1 = "org";
	public static final String ID_SUFFIX_2 = "dup";
	
	
	public GeneratedDatasetSplitter(String pathToFile, boolean hasHeader){
		super(pathToFile, hasHeader);
	}
	
	public GeneratedDatasetSplitter(String pathToFile, boolean hasHeader, int idColumn){
		super(pathToFile, hasHeader, idColumn);
	}
	
	private static Side getSideOfRecord(String id){
		if (id.contains(ID_SUFFIX_1)){
			return Side.LEFT;

		}
		else if (id.contains(ID_SUFFIX_2)){
			return Side.RIGHT;
		}
		else{
			System.out.println(id);
			throw new RuntimeException();
		}
	}
	
	private void cleanRecordId(List<CSVRecordEntry> records){
		for (final CSVRecordEntry record : records){
			final String id = record.getAttributes().get(idColumn);
			final String[] splittedId = id.split("-");
			final String newId = splittedId[1];
			record.getAttributes().set(idColumn, newId);
		}
	}
	
	private Map<Side, List<CSVRecordEntry>> groupBySide(List<CSVRecordEntry> records){
		return records
			.stream()
			.collect(
				Collectors.groupingBy(
					rec -> getSideOfRecord(rec.getAttributes().get(idColumn)), 
					Collectors.toCollection(ArrayList::new))
			);
	}

	
	public void splitByIdAndWrite() throws IOException{
		final List<CSVRecordEntry> records = readAndParseFile();
		
		final Map<Side, List<CSVRecordEntry>> groupedRecords = groupBySide(records);
		
		final List<CSVRecordEntry> leftSide = groupedRecords.get(Side.LEFT);
		final List<CSVRecordEntry> rightSide = groupedRecords.get(Side.RIGHT);
		
		cleanRecordId(leftSide);
		cleanRecordId(rightSide);
	
		final CSVWriter csvWriterOrg = new CSVWriter(this.getOutputPath() + ID_SUFFIX_1);
		final CSVWriter csvWriterDup = new CSVWriter(this.getOutputPath() + ID_SUFFIX_2);
		
		if (hasHeader){
			csvWriterOrg.writeCSVRecordsWithHeader(leftSide, this.header.keySet());
			csvWriterDup.writeCSVRecordsWithHeader(rightSide, this.header.keySet());
		}
		else{
			csvWriterOrg.writeCSVRecords(leftSide);
			csvWriterDup.writeCSVRecords(rightSide);
		}
	}
	
	@Override
	protected String opName() {
		return "";
	}
	
	public static void main(String[] args) throws IOException{
		final String file = "/home/mfranke/Schreibtisch/M1or.csv";
		final boolean hasHeader = false;
		
		final GeneratedDatasetSplitter splitter = new GeneratedDatasetSplitter(file, hasHeader);
		splitter.splitByIdAndWrite();
	}
}