package dbs.pprl.toolbox.client.blocking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVRecord;

import dbs.pprl.toolbox.client.common.RecordEntryTransformer;
import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;
import dbs.pprl.toolbox.utils.CSVReader;
import dbs.pprl.toolbox.utils.CSVWriter;

public class Blocking {

	public static final int DEFAULT_ID_COLUMN = 0;
	public static final String OUTPUT_SUFFIX = ".csv";
	
	private final String pathToFile;
	private final boolean hasHeader;
	private final int idColumn;
	
	final List<BlockingKeyDefinition> bkList;
	
	public Blocking(String pathToFile, boolean hasHeader){
		this(pathToFile, hasHeader, DEFAULT_ID_COLUMN);
	}
	
	public Blocking(String pathToFile, boolean hasHeader, int idColumn){
		this.pathToFile = pathToFile;
		this.hasHeader = hasHeader;
		this.idColumn = idColumn;
		this.bkList = new ArrayList<BlockingKeyDefinition>();
	}

	
	private List<CSVRecordEntry> readFile() throws IOException{
		final CSVReader csvReader = new CSVReader(pathToFile, hasHeader);
		final List<CSVRecord> csvRecords = csvReader.read();
		return RecordEntryTransformer.from(csvRecords, this.idColumn);
	}
	
	
	private void writeFile(List<CSVRecordEntry> records) throws IOException{
		final CSVWriter csvWriter = new CSVWriter(this.getOutputPath());
		csvWriter.writeCSVRecords(records);
	}

	private String getOutputPath(){
		final String[] pathComponents = this.pathToFile.split("\\.");
		StringBuilder builder = new StringBuilder();
		builder.append(pathComponents[0]);
		builder.append("_bk");
		builder.append(OUTPUT_SUFFIX);
		return builder.toString();
	}
	
	public void addBlocker(BlockingKeyDefinition... bks){
		for (final BlockingKeyDefinition bk : bks){
			bkList.add(bk);
		}
	}
	
	public void addBlocker(List<BlockingKeyDefinition> bks){
		for (final BlockingKeyDefinition bk : bks){
			bkList.add(bk);
		}
	}
		
	public void block() throws IOException{
		final List<CSVRecordEntry> records = this.readFile();

		final StandardBlocker sb = new StandardBlocker(bkList);
		final Map<CSVRecordEntry, List<String>> blocks = sb.block(records);
		
		final List<CSVRecordEntry> result = new ArrayList<>(blocks.size());
		
		for (final Entry<CSVRecordEntry, List<String>> rec : blocks.entrySet()){
			final CSVRecordEntry newEntry = new CSVRecordEntry();
			
			final CSVRecordEntry entry = rec.getKey();
			final List<String> bks = rec.getValue();
			
			final String id = entry.getAttribute(this.idColumn);
			newEntry.addAttribute(id);
			
			for (final String bk : bks){
				newEntry.addAttribute(bk);
			}
			result.add(newEntry);			
		}
		this.writeFile(result);
	}
	
	public static void main(String[] args) throws Exception{
		final Blocking blocking = new Blocking("/home/mfranke/Schreibtisch/M1.csv", true);
		final ConfigLoaderBlocking confBlocking = new ConfigLoaderBlocking();
		final List<BlockingKeyDefinition> bkDefs = confBlocking.load();
		
		blocking.addBlocker(bkDefs);
		
		/*
		// How to construct from config file?
		final BlockingKeyDefinition bk1Def = new BlockingKeyDefinition();
		bk1Def.set(2, new SoundexBlockingFunction());
		
		final BlockingKeyDefinition bk2Def = new BlockingKeyDefinition();
		bk2Def.set(1, new SoundexBlockingFunction());
		bk2Def.set(2, new SoundexBlockingFunction());
		
		blocking.addBlocker(bk1Def, bk2Def);
		*/
		blocking.block();
	}
}
