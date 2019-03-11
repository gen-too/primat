package dbs.pprl.toolbox.client.preprocessing;

import java.io.IOException;
import java.util.BitSet;
import java.util.List;
import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;
import dbs.pprl.toolbox.lu.similarityCalculation.JaccardSimilarityCalculator;
import dbs.pprl.toolbox.utils.BitSetUtils;

// REWORK: Isn't it part of LU-side?
public class BloomFilterBase64Converter{

	public static final String TRANSFORMATION_SHORTCUT = "base64";
	
	private final int bfCol;
		
	public BloomFilterBase64Converter(String pathToFile, boolean hasHeader, int idColumn, int bfCol){
		super(pathToFile, hasHeader, idColumn);
		this.bfCol = bfCol;
	}
	
	private void convertRecord(CSVRecordEntry record){
		final String bits = record.getAttribute(this.bfCol);
//		System.out.println(bits);
		final BitSet bitset = BitSetUtils.fromBinary(bits);
//		System.out.println(bitset);
//		System.out.println(bitset.size());
//		System.out.println(bitset.length());
		
		final String base64 = BitSetUtils.toBase64(bitset);
//		System.out.println(base64);	
		
		final BitSet readBitSet = BitSetUtils.fromBase64(base64);
//		System.out.println(readBitSet);
//		System.out.println(readBitSet.size());
//		System.out.println(readBitSet.length());
		
//		System.out.println(BitSetUtils.toStringBinary(bitset));
		
		for (int i = 1000; i < 1800; i++){
			if (bitset.get(i) != readBitSet.get(i)){
				System.out.println(BitSetUtils.toBinary(bitset));
				System.out.println(BitSetUtils.toBinary(readBitSet));
				System.out.println(JaccardSimilarityCalculator.calculateJaccardSimilarity(bitset, readBitSet));
			};
		}
		
		
		
		
		
		record.setAttribute(this.bfCol, base64);
	}
	
	public List<CSVRecordEntry> convert() throws IOException{
		final List<CSVRecordEntry> records = this.readAndParseFile();
		
		for (final CSVRecordEntry rec : records){
			this.convertRecord(rec);
			
		}
		
		return records;
	}
	
	public void convertAndWrite() throws IOException{	
		final List<CSVRecordEntry> records = this.convert();
		this.write(records);
	}
	
	@Override
	protected String opName() {
		return TRANSFORMATION_SHORTCUT;
	}
	
	public static void main(String[] args) throws IOException{
		final String file = "/home/mfranke/Schreibtisch/M1_dup.csv";
		final boolean hasHeader = false;
		
		final BloomFilterBase64Converter conv = new BloomFilterBase64Converter(file, hasHeader, 0, 1);
		conv.convertAndWrite();
	}
	
	
}
