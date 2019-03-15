package dbs.pprl.toolbox.client.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVRecord;

import dbs.pprl.toolbox.client.data.CSVRecordWrapper;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.lu.blocking.Side;
import dbs.pprl.toolbox.utils.CSVReader;
import dbs.pprl.toolbox.utils.CSVWriter;

public class GeneratedDatasetSplitter{

	public static final String ID_SUFFIX_1 = "org";
	public static final String ID_SUFFIX_2 = "dup";
	
	private String pathToFile;
	private int idColumn;
	
	public GeneratedDatasetSplitter(String pathToFile, int idColumn){
		this.pathToFile = pathToFile;
		this.idColumn = idColumn;
	}
	
	private List<Record> readFile() throws IOException{
		final CSVReader csvReader = new CSVReader(pathToFile, false);
		final List<CSVRecord> csvRecords = csvReader.read();
		final List<Record> pojoRecords = CSVRecordWrapper.from(csvRecords, idColumn);
		return pojoRecords;
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
	
	private void cleanRecordId(List<Record> records){
		for (final Record record : records){
			final String id = record.getAttributes().get(idColumn);
			final String[] splittedId = id.split("-");
			final String newId = splittedId[1];
			record.getAttributes().set(idColumn, newId);
		}
	}
	
	private Map<Side, List<Record>> groupBySide(List<Record> records){
		return records
			.stream()
			.collect(
				Collectors.groupingBy(
					rec -> getSideOfRecord(rec.getAttributes().get(idColumn)), 
					Collectors.toCollection(ArrayList::new))
			);
	}
	
	private void writeFile(List<Record> leftRecords, List<Record> rightRecords) throws IOException{
		final CSVWriter csvWriterOrg = new CSVWriter(this.pathToFile + ID_SUFFIX_1);
		csvWriterOrg.writeCSVRecords(leftRecords);
		final CSVWriter csvWriterDup = new CSVWriter(this.pathToFile + ID_SUFFIX_2);
		csvWriterDup.writeCSVRecords(rightRecords);
	}
	
	public void splitByIdAndWrite() throws IOException{
		final List<Record> records = readFile();
		
		final Map<Side, List<Record>> groupedRecords = groupBySide(records);
		
		final List<Record> leftSide = groupedRecords.get(Side.LEFT);
		final List<Record> rightSide = groupedRecords.get(Side.RIGHT);
		
		this.cleanRecordId(leftSide);
		this.cleanRecordId(rightSide);
	
		this.writeFile(leftSide, rightSide);
	}
	

	
	public static void main(String[] args) throws IOException{
		final String file = "/home/mfranke/Schreibtisch/M1or.csv";
		
		final GeneratedDatasetSplitter splitter = new GeneratedDatasetSplitter(file, 0);
		splitter.splitByIdAndWrite();
	}
}