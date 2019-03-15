package dbs.pprl.toolbox.client.tools;

import java.io.IOException;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.data.CSVRecordWrapper;
import dbs.pprl.toolbox.utils.BitSetUtils;
import dbs.pprl.toolbox.utils.CSVReader;
import dbs.pprl.toolbox.utils.CSVWriter;

public class BloomFilterBase64Converter{

	private String pathToFile;
	private int idColumn;
	private int bfColumn;
	
	public BloomFilterBase64Converter(String pathToFile, int idColumn, int bfColumn){
		this.pathToFile = pathToFile;
		this.idColumn = idColumn;
		this.bfColumn = bfColumn;
	}
	
	private List<Record> readFile() throws IOException{
		final CSVReader csvReader = new CSVReader(pathToFile, false);
		final List<CSVRecord> csvRecords = csvReader.read();
		final CSVRecordWrapper wrapper = new CSVRecordWrapper(attrMap)
		final List<Record> pojoRecords = CSVRecordWrapper.from(csvRecords);
		return pojoRecords;
	}
	
	private void writeFile(List<Record> records) throws IOException{
		final CSVWriter csvWriter = new CSVWriter(pathToFile);
		csvWriter.writeCSVRecords(records);
	}
	
	public void convertToBase64() throws IOException, AttributeParseException{
		final List<Record> records = this.readFile();
		
		for (final Record rec : records){
			final String bfString = rec.getAttribute(bfColumn).getStringValue();
			final BitSet bf = BitSetUtils.fromBinaryBigEndian(bfString);
			final String bf64 = BitSetUtils.toBase64LittleEndian(bf);
			rec.getAttribute(bfColumn).setValue(bf64);
		}
		
		this.writeFile(records);
	}
		
	
	public static void main(String[] args) throws IOException{
		final String file = "/home/mfranke/Schreibtisch/M1_dup.csv";
		
		final BloomFilterBase64Converter conv = new BloomFilterBase64Converter(file, 0, 1);
		conv.convertToBase64();
	}
	
	
}
