package dbs.pprl.toolbox.client.lisard;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import dbs.pprl.toolbox.client.data.attributes.IdAttribute;
import dbs.pprl.toolbox.client.data.attributes.StringAttribute;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.lisard.attributes.Attribute;
import dbs.pprl.toolbox.client.lisard.attributes.IndipendentAttribute;
import dbs.pprl.toolbox.client.lisard.attributes.NonIndipendentAttribute;
import dbs.pprl.toolbox.client.lisard.common.CsvInputHandler;
import dbs.pprl.toolbox.client.lisard.lookup.FrequencyLookup;
import dbs.pprl.toolbox.client.lisard.lookup.SimpleDependentBasicRandomLookup;


public class Generator {

	private final List<Attribute> attributes;
	private final IdManager idManager;
	private final Set<Integer> familyAttributePositions;
	
	
	public Generator(List<Attribute> attributes){
		this.attributes = resolveAttributeDependencies(attributes);
		this.idManager = new IdManager();
		this.familyAttributePositions = new HashSet<>();
		for (int i = 0; i < this.attributes.size(); i++){
			final Attribute att = this.attributes.get(i);
			if (att.isFamilyAttribute()){
				this.familyAttributePositions.add(i);
			}
		}
	}
	
	private static List<Attribute> resolveAttributeDependencies(List<Attribute> attributes){
		final ArrayDeque<Attribute> attrToProcess = new ArrayDeque<Attribute>(attributes);
		final LinkedHashSet<Attribute> result = new LinkedHashSet<Attribute>(attributes.size());
		
		while (!attrToProcess.isEmpty()){
			final Attribute attr = attrToProcess.pop();
			
			if (attr instanceof NonIndipendentAttribute){		
				final List<Attribute> dependencies = ((NonIndipendentAttribute) attr).getDependentAttributes();
					
				if (result.containsAll(dependencies)){
					// Ok, dependencies are ok.
					result.add(attr);
				}
				else{
					// No, cannot be generated at this point; skip this one.
					attrToProcess.addLast(attr);
				}
			}
			else if (attr instanceof IndipendentAttribute){
				result.add(attr);
			}	
			else {
				throw new RuntimeException("");
			}
		}
		return new ArrayList<Attribute>(result);
	}
	
	public static String generateNonIndipendentAttribute(NonIndipendentAttribute attr, Map<Attribute, String> dependencyValues, long seed){
		final Attribute dependency = attr.getDependentAttribute();
		final String dependencyValue = dependencyValues.get(dependency);
		attr.setValueOfDependentAttribute(dependencyValue);
		return attr.getValue(seed);
	}
	
	private void generateAttributes(Record rec){
		this.generateAttributes(rec, null);
	}
	
	public void generateAttributes(Record rec, List<dbs.pprl.toolbox.client.data.attributes.Attribute<?>> familyAttributes){
		final Map<Attribute, String> attrValueMapping = new HashMap<Attribute, String>();
		
		final long seed = this.idManager.getCurrentRecordNumber() + 42;
		
		for (int i = 0; i < this.attributes.size(); i++){
			final Attribute attr = this.attributes.get(i);
			
			final String value;
			
			if (familyAttributes != null && !familyAttributes.isEmpty() && attr.isFamilyAttribute()){
				// This one is a family attribute that should be copied
				value = familyAttributes.get(i + 1).getStringValue(); // since Id is part of familyAttributes
			}
			else{
				// No family attribute or independent record generation process
				if (attr instanceof NonIndipendentAttribute){					
					value = generateNonIndipendentAttribute((NonIndipendentAttribute) attr, attrValueMapping, seed);	
				}
				else if (attr instanceof IndipendentAttribute){
					 value = ((IndipendentAttribute) attr).getValue(seed);
				}
				else{
					throw new RuntimeException("");
				}
			}
			rec.add(new StringAttribute(value));
			attrValueMapping.put(attr, value);
		}
	}
	
	public Record generateIndipendentRecord(char dataOwner){
		final Record rec = new Record();
		
		final String id = this.idManager.getIndipendentRecordId(dataOwner);
		
		rec.add(new IdAttribute(id));
		this.generateAttributes(rec);
		
		return rec;
	}
	
	public static int getFamilySize(long seed){
		final List<Integer> familyDistro = new ArrayList<>();
		familyDistro.add(1);
		familyDistro.add(1);
		familyDistro.add(1);
		familyDistro.add(1);
		familyDistro.add(2);
		familyDistro.add(2);
		familyDistro.add(3);
		Collections.shuffle(familyDistro, new Random(32));
		
		final Random rnd = new Random(seed + 33);
		final int rndIndex = rnd.nextInt(familyDistro.size());
		final int familySize = familyDistro.get(rndIndex);
		return familySize;
	}
	
	public List<Record> generateFamily(FrequencyLookup familyDistro, char dataOwner){
		final Record firstMember = this.generateIndipendentRecord(dataOwner);	
		final List<dbs.pprl.toolbox.client.data.attributes.Attribute<?>> familyAttributes = firstMember.getAttributes();
		
		familyDistro.setSeed(this.idManager.getCurrentFamilyNumber() + dataOwner);
		final int familySize = Integer.parseInt(familyDistro.getValue());
		System.out.println(familySize);
		
		final List<Record> family = new ArrayList<>(familySize);
		family.add(firstMember);
		
		for (int i = 0; i < familySize; i++){
			final Record member = new Record();
			final String id = this.idManager.getFamilyRecordId(dataOwner);
			member.add(new IdAttribute(id));
			
			this.generateAttributes(member, familyAttributes);
			family.add(member);
			
		}
		return family;
	}
	
	public List<Record> generateDataset(int sourceIndipendentRecords, double familyRate, FrequencyLookup familyDistro, char dataOwner){
		final List<Record> rndRecords = new ArrayList<>(sourceIndipendentRecords);
		
		final int singles = (int) (sourceIndipendentRecords * (1 - familyRate));
		final int families = sourceIndipendentRecords - singles;
				
		// Singles
		for (int i = 0; i < singles; i++){
			final Record rndRec = this.generateIndipendentRecord(dataOwner);
			rndRecords.add(rndRec);
		}

		// Families
		// IMPORTANT: Can produce more or less records than specified!
		for (int i = 0; i < families; i++){
			final List<Record> rndFamRec = this.generateFamily(familyDistro, dataOwner);
			rndRecords.addAll(rndFamRec);
			i = i + rndFamRec.size();		
		}
		
		// Generate some more singles if to few records
		for (int i = rndRecords.size(); i < sourceIndipendentRecords; i++) {
			final Record rndRec = this.generateIndipendentRecord(dataOwner);
			rndRecords.add(rndRec);
		}
		
		// Delete some family members if to much records
		for (int i = rndRecords.size(); i > sourceIndipendentRecords; i--) {
			rndRecords.remove(rndRecords.size()-1);
		}
		
		return rndRecords;
	}
	
	
	public void generateData(int[][] configuration) throws IOException {
		final FrequencyLookup familyDistro = CsvInputHandler.readFrequencyLookup("/home/mfranke/Schreibtisch/family_distro.csv", false);
		
		final int parties = configuration.length;
		
		final ListValuedMap<Integer, List<Record>> dbs = new ArrayListValuedHashMap<>();
		
		for (int i = 0; i < parties; i++) {
			char currentParty = (char) ('A' + i);
			final List<Record> db_i = this.generateDataset(configuration[i][i], 0, familyDistro, currentParty);
			dbs.put(i, db_i);
			for (final Record e : db_i) {
				System.out.println(e);
			}
			System.out.println();
		}
		System.out.println("Base done");
		for (int i = 0; i < parties; i++) {
			char currentPartyId = (char) ('A' + i);
			
			for (int j = 0; j < parties; j++) {
				if (i != j) {
					final List<Record> dupFromParty = dbs.get(j).get(0);
					final int overlap = configuration[i][j];
					final List<Record> dups = this.shuffle(currentPartyId, dupFromParty, overlap);
					dbs.put(i, dups);
					for (final Record d : dups) {
						System.out.println(d);
					}
					System.out.println();
				}
				
			}
		}
		
		/*
		final List<CSVRecordEntry> recordsA = this.generateDataset(25, 0.4, familyDistro, 'A');
		for (CSVRecordEntry r : recordsA) {
			System.out.println(r);
		}
		
		System.out.println();
		
		final List<CSVRecordEntry> recordsB = this.generateDataset(25, 0.4, familyDistro, 'B');
		for (CSVRecordEntry r : recordsB) {
			System.out.println(r);
		}
		
		final List<CSVRecordEntry> recordsAB = this.shuffle('A', recordsB, 2);
		final List<CSVRecordEntry> recordsBA = this.shuffle('B', recordsA, 4);
		*/
	}
	
	/*
	public void generateData() throws IOException{	
		char currentParty = 'A';
		
		int recordsPartyA = 1000;
		
		final List<CSVRecordEntry> db_A = generateDataset(recordsPartyA, 0d, currentParty);
		
		// ----------
		
		currentParty++;
		
		int recordsPartyB = 1000;
		int overlapPartyBtoA = 500;
		
		int individualRecordsPartyB = recordsPartyB - overlapPartyBtoA;
		
		final List<CSVRecordEntry> db_B = generateDataset(individualRecordsPartyB, 0d, currentParty);
		
		// ----
		
		final int startSeed = 10;
		for (int i = 0; i < overlapPartyBtoA; i++){
			final Random rnd = new Random(startSeed + i);
			final int rndIndex = rnd.nextInt(db_A.size());
			final CSVRecordEntry rndRecord = db_A.get(rndIndex);
		}
		
		
		// ----------
	}
	*/
	
	public List<Record> shuffle(char dataOwner, List<Record> right, int overlap) {
		if (overlap > right.size()) {
			throw new RuntimeException();
		}
		
		final List<Record> copies = new ArrayList<Record>(overlap);
		
		final Random rnd = new Random(dataOwner);
		final int[] indices = rnd.ints(0, right.size()).distinct().limit(overlap).toArray();
		
		for (final int i : indices) {;
			final Record oldRec = right.get(i);
			final RecordId oldId = RecordId.from(oldRec.getId());
			final String newId = idManager.generateDuplicateRecordId(oldId, dataOwner);

			final Record copy = new Record(new IdAttribute(newId), new ArrayList<>(oldRec.getAttributes()));
			
			copies.add(copy);
		}
		
		return copies;
	}
	
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException{
		final List<Attribute> attributesToGenerate = new ArrayList<>();
				
		final FrequencyLookup firstNameLookup = CsvInputHandler.readFrequencyLookup("/home/mfranke/Schreibtisch/firstnames.csv", false);
		final IndipendentAttribute firstName = new IndipendentAttribute("first_name", false, firstNameLookup);
		attributesToGenerate.add(firstName);
		
		final FrequencyLookup lastNameLookup = CsvInputHandler.readFrequencyLookup("/home/mfranke/Schreibtisch/lastnames.csv", false);
		final IndipendentAttribute lastName = new IndipendentAttribute("last_name", true, lastNameLookup);
		attributesToGenerate.add(lastName);
		
		final FrequencyLookup zipLookup = CsvInputHandler.readFrequencyLookup("/home/mfranke/Schreibtisch/zip.csv", false);
		final IndipendentAttribute zip = new IndipendentAttribute("zip", true, zipLookup);
//		attributesToGenerate.add(zip);
		
		
		final SimpleDependentBasicRandomLookup cityLookup = CsvInputHandler.readSimpleDependentBasicRandomLookup("/home/mfranke/Schreibtisch/zip_to_city.csv", false, false);
		final NonIndipendentAttribute city = new NonIndipendentAttribute("city", true, zip, cityLookup);
//		attributesToGenerate.add(city);
		
		final FrequencyLookup yearOfBirthLookup = CsvInputHandler.readFrequencyLookup("/home/mfranke/Schreibtisch/dob_year.csv", false);
		final IndipendentAttribute yearOfBirth = new IndipendentAttribute("year_of_birth", false, yearOfBirthLookup);
//		attributesToGenerate.add(yearOfBirth);
		
		final FrequencyLookup monthOfBirthLookup = CsvInputHandler.readFrequencyLookup("/home/mfranke/Schreibtisch/dob_month.csv", false);
		final IndipendentAttribute monthOfBirth = new IndipendentAttribute("month_of_birth", false, monthOfBirthLookup);
//		attributesToGenerate.add(monthOfBirth);
						
		final SimpleDependentBasicRandomLookup dayOfBirthLookup = CsvInputHandler.readSimpleDependentBasicRandomLookup("/home/mfranke/Schreibtisch/day_month_dependency.csv", false, false);
		final NonIndipendentAttribute dayOfBirth = new NonIndipendentAttribute("day_of_birth", false, monthOfBirth, dayOfBirthLookup);
//		attributesToGenerate.add(dayOfBirth);
		
		final Generator generator = new Generator(attributesToGenerate);
		
		final int[][] config = 
			{
					{3000}
//				{10,  2,  2,  2},
//				{ 1, 10,  1,  1},
//				{ 0,  5, 10,  0},
//				{ 0,  0,  5, 10}
			};
		
		generator.generateData(config);
		
		
		
	}
}