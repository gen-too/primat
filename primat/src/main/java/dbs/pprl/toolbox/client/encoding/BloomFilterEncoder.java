package dbs.pprl.toolbox.client.encoding;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import dbs.pprl.toolbox.client.encoding.attributes.Attribute;
import dbs.pprl.toolbox.client.encoding.attributes.AttributeType;
import dbs.pprl.toolbox.client.encoding.bloomfilter.BloomFilter;
import dbs.pprl.toolbox.client.encoding.transformer.Transformer;
import dbs.pprl.toolbox.client.encoding.transformer.TransformerChain;
import dbs.pprl.toolbox.client.encoding.transformer.TransformerDefinition;
import dbs.pprl.toolbox.client.preprocessing.CSVOperator;
import dbs.pprl.toolbox.client.encoding.transformer.QGramTransformer;
import dbs.pprl.toolbox.client.encoding.transformer.SubstringTransformer;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.utils.CSVWriter;

//TODO: !!! Rework: Split into Mapper/Transformer and Encoder classes
public class BloomFilterEncoder extends CSVOperator{

	private String outputFile;
	private ColumnAttributTypeMapping attributeColumns;
	
		
	public BloomFilterEncoder(String pathToFile, boolean hasHeader, ColumnAttributTypeMapping attributeColumns){
		super(pathToFile, hasHeader, attributeColumns.getIdColumn());
		
		this.attributeColumns = attributeColumns;		
		this.outputFile = pathToFile.split("\\.")[0] + "_coded.csv";
	}
	
	public void doIt() throws IOException, InvalidKeyException, NoSuchAlgorithmException{
		final List<CSVRecord> csvRecords = this.readFile();
		final List<Record> records = this.transformToPojo(csvRecords);
		System.out.println(records.get(0));
			
		final Map<String, Map<Integer, Set<String>>> mappedRecords = this.transform(records);
		System.out.println("After map");
		final List<EncodedRecord> encodedRecs = this.encode(mappedRecords);
		System.out.println(encodedRecs.size());
		final CSVWriter writer = new CSVWriter(outputFile);
		writer.writeEncodedRecords(encodedRecs);
	}
	
	

	
	private List<Record> transformToPojo(List<CSVRecord> records){
		final List<Record> result = new ArrayList<Record>(records.size());

		for (final CSVRecord csvRecord : records){
			final Record record = this.transform(csvRecord);
			result.add(record);
		}
		
		return result;
	}
	
	private Record transform(CSVRecord record){
		final Record transformedRecord = new Record();
		
		final int idCol = attributeColumns.getIdColumn();
		final String id = record.get(idCol);
		transformedRecord.setId(id);
		
		final Map<Integer, AttributeType> mapping = attributeColumns.getMappingWithoutIdColumn();
		
		for (final Entry<Integer, AttributeType> mappingEntry : mapping.entrySet()){
			final int column = mappingEntry.getKey();
			final AttributeType attrType = mappingEntry.getValue();
					
			final String attributeValue = record.get(column);
			final String attributeName = (header == null) ? null : header.getKey(column);
						
			final Attribute<?> attr = attrType.constructAttribute();
			attr.setValue(attributeValue);
			attr.setName(attributeName);
			
			transformedRecord.add(column, attr);
		}	
		
    	return transformedRecord;
	}
	
	
	
	
	
	
	// Rec-ID, Attr (Col. / Name) , Token-Set
	private Map<String, Map<Integer, Set<String>>> transform(List<Record> records){
		final Map<String, Map<Integer, Set<String>>> result = new HashMap<>(records.size());
		for (final Record rec : records){
			final Map<Integer, Set<String>> mappedRecordAttributes = transform(rec);
//			System.out.println(mappedRecordAttributes);
			result.put(rec.getId(), mappedRecordAttributes);
		}
		return result;
	}
	
	private Map<Integer, Set<String>> transform(Record record){
		// TODO: This should be an global input
		// -------------------------------------
		final TransformerDefinition transDef = new TransformerDefinition();
		final boolean padding = true;
		final int q = 3;
		transDef.setTransformer(1, new QGramTransformer(q, padding));
		transDef.setTransformer(2, new QGramTransformer(q, padding));
		transDef.setTransformer(3, new QGramTransformer(q, padding));
		transDef.setTransformer(4, new QGramTransformer(q, padding));
//		transDef.setTransformer(5, new QGramTransformer(q, padding));
		
//		final TransformerChain stc = new TransformerChain();
//		stc.add(new SubstringTransformer(1, 3));
//		stc.add(new SubstringTransformer(2, 4));
//		stc.add(new SubstringTransformer(1, 4));
//		transDef.setTransformer(10, stc);		
		// -------------------------------------
		
		final Map<Integer, Attribute<?>> attributes = record.getAttributes();
		
		final Map<Integer, Set<String>> result = new HashMap<>(attributes.size());
		
		
		for (final Integer attr : attributes.keySet()){
			if (transDef.hasTransformer(attr)){
				final Transformer transformer = transDef.getTransformer(attr);
				final Attribute<?> value = attributes.get(attr);
				final Set<String> transformedAttr = transformer.transform(value);
//				System.out.println(attr +" "+ transformedAttr);
				result.put(attr, transformedAttr);
			}
			else{
				// don't map / use this attribute !
			}
		}

		return result;
	}
	
	private List<EncodedRecord> encode(Map<String, Map<Integer, Set<String>>> mappedRecords) throws InvalidKeyException, NoSuchAlgorithmException{
		System.out.println("ENCODE");
		final List<EncodedRecord> result = new ArrayList<>(mappedRecords.size());
		final DescriptiveStatistics stat = new DescriptiveStatistics();
	
		for (final Entry<String, Map<Integer, Set<String>>> mappedRec : mappedRecords.entrySet()){
			final String recId = mappedRec.getKey();
//			System.out.println("RecId: " + recId);
			final Map<Integer, Set<String>> mappedAttributes = mappedRec.getValue();
//			System.out.println(mappedAttributes);
			final EncodedRecord encodedRec = this.encode(recId, mappedAttributes);
			result.add(encodedRec);
			System.out.println(result.size());
//			System.out.println(encodedRec.getBitVector().cardinality());
			stat.addValue(encodedRec.getBitVector().cardinality());
		}
		
		System.out.println(stat.getMean());
		return result;
	}
	
	private EncodedRecord encode(String recId, Map<Integer, Set<String>> mappedAttributes) throws InvalidKeyException, NoSuchAlgorithmException{
		final EncodedRecord encodedRec = new EncodedRecord();
		encodedRec.setId(recId);

		final BloomFilter recBf = this.encodeAttributesIntoBf(mappedAttributes);
		encodedRec.setBitVector(recBf.getBitVector());
		
		return encodedRec;
	}
	
	private BloomFilter encodeAttributesIntoBf(Map<Integer, Set<String>> mappedAttributes) throws InvalidKeyException, NoSuchAlgorithmException{
		//TODO: GLOBAL !
		final BloomFilter bf = new BloomFilter(1024);
		
		// TODO: This should be an global input
		// -------------------------------------
		
		final Map<Integer, Integer> hashFunctionsForAttributes = new HashMap<Integer, Integer>();
		final int k = 17;
		hashFunctionsForAttributes.put(1, k);
		hashFunctionsForAttributes.put(2, k);
		hashFunctionsForAttributes.put(3, k);
		hashFunctionsForAttributes.put(4, k);
//		hashFunctionsForAttributes.put(5, k);
		
		// -------------------------------------
		
		for (final Entry<Integer, Set<String>> attr : mappedAttributes.entrySet()){
			final Integer attrId = attr.getKey();
//			System.out.println("AttrID: " + attrId);
			final Set<String> tokenSet = attr.getValue();
			final Integer hashFunctions = hashFunctionsForAttributes.get(attrId);
			final String keyForAttr = attrId.toString();
			
			bf.addMappedAttribute(tokenSet, hashFunctions);
			
//			bf.addMappedAttributeRandomHashing(tokenSet, hashFunctions, keyForAttr);
		}

//		bf.foldXor();
//		bf.foldXor();
		
		return bf;
	}

	public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException{
		final ColumnAttributTypeMapping columnTypeMapping = new ColumnAttributTypeMapping(0);
		columnTypeMapping.setTypeString(1);
		columnTypeMapping.setTypeString(2);
		columnTypeMapping.setTypeString(3);
		columnTypeMapping.setTypeString(4);
//		columnTypeMapping.setTypeString(5);
		
		
		final BloomFilterEncoder encoder = 
			new BloomFilterEncoder(
//					"/media/mfranke/HDD/LSH_paper/M1/M1__dup.csv", 
					"/home/mfranke/Schreibtisch/M1.csv",
					true,
					columnTypeMapping
			);
		
		encoder.doIt();
	}
}