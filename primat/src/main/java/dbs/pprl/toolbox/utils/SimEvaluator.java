package dbs.pprl.toolbox.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.lu.similarityFunctions.JaccardSimilarity;
import dbs.pprl.toolbox.lu.similarityFunctions.StrictOverlapSimilarity;

public class SimEvaluator {

	public static List<EncodedRecord> readRecordsFromFile(String path) throws IOException{
		final CSVReader csvReader = new CSVReader(path, false);
		final List<CSVRecord> stringRecords = csvReader.read();
		final CSVToEncodedRecordTransformer csvPojoTransformer = new CSVToEncodedRecordTransformer();
		return csvPojoTransformer.transform(stringRecords);
	}
	
	
	public static List<CandidatePair> getMatches(List<EncodedRecord> encodedRecordsA, List<EncodedRecord> encodedRecordsB){
		final List<EncodedRecord> allRecords = new ArrayList<>(encodedRecordsA.size() + encodedRecordsB.size());
		allRecords.addAll(encodedRecordsA);
		allRecords.addAll(encodedRecordsB);
		
		final Map<String, List<EncodedRecord>> recordsGroupedById = allRecords
				.stream()
				.collect(
					Collectors.groupingBy(EncodedRecord::getId, Collectors.toList())
				);
			
		final List<CandidatePair> matches = recordsGroupedById
				.entrySet()
				.stream()
				.filter(
					group -> 
					group.getValue().size() == 2)
				.map(
					group -> 
					new CandidatePair(group.getValue().get(0), group.getValue().get(1))
				)
				.collect(Collectors.toList());
		
		return matches;
		/*
		
		final DescriptiveStatistics statSimJaccard = new DescriptiveStatistics();
		final DescriptiveStatistics statSimOverlap = new DescriptiveStatistics();
		
		for (CandidatePair cand : matches){
				double simJaccard = JaccardSimilarityCalculator.calculateJaccardSimilarity(cand);
				double simOverlap = OverlapSimilarityCalculator.calculateOverlapSimilarity(cand);
				
				simJaccard = new BigDecimal(simJaccard).setScale(2, RoundingMode.HALF_UP).doubleValue();
				simOverlap = new BigDecimal(simOverlap).setScale(2, RoundingMode.HALF_UP).doubleValue();
							
				statSimJaccard.addValue(simJaccard);
				statSimOverlap.addValue(simOverlap);
			}
			
		final int[] simJaccardHisto = calcSimJaccardHistogram(statSimJaccard, 0, 1, 20);
		final int[] simOverlapHisto = calcSimOverlapHistogram(statSimOverlap);
		
		System.out.println("Jaccard-Similarity: " + Arrays.toString(simJaccardHisto));
		System.out.println("Overlap-Similarity: " + Arrays.toString(simOverlapHisto));
		*/
	}
	
	
	
	public static List<CandidatePair> getNonMatches(List<EncodedRecord> encodedRecordsA, List<EncodedRecord> encodedRecordsB){
		final List<CandidatePair> nonMatches = new ArrayList<>(25_000_000);
		
		final Random rnd = new Random();
		for (int i = 0; i < 25_000_000; i++){
			String idA = "";
			String idB = "";
			
			EncodedRecord ar = null;
			EncodedRecord br = null;
			
			while (idA.equals(idB)){
				final int a = rnd.nextInt(encodedRecordsA.size());
				final int b = rnd.nextInt(encodedRecordsB.size());
				
				ar = encodedRecordsA.get(a);
				br = encodedRecordsB.get(b);
				
				idA = ar.getId();
				idB = br.getId();
			}
			
			final CandidatePair pair = new CandidatePair(ar,br);
			nonMatches.add(pair);
		}
		
		return nonMatches;
	}		
	
	public static void determineSimilarities(List<CandidatePair> pairs, boolean matches, BitSet nonFrequentBitMask, BitSet frequentOneBitMask, BitSet frequentZeroBitMask){
		final DescriptiveStatistics statSimJaccard = new DescriptiveStatistics();
		final DescriptiveStatistics statSimOverlap = new DescriptiveStatistics();
		
		final DescriptiveStatistics statSimOverlapJaccard = new DescriptiveStatistics();
		
		final DescriptiveStatistics statSimNonFrequent = new DescriptiveStatistics();
		final DescriptiveStatistics statSimFrequentOneBits = new DescriptiveStatistics();
		final DescriptiveStatistics statSimFrequentZeroBits = new DescriptiveStatistics();
		
		int count = 0;
		
		int nonFrequentSimGreaterThanNormalSimCounter = 0;
		int frequent1SimGreaterThanNormalSimCounter = 0;
		
		for (final CandidatePair pair : pairs){
			final EncodedRecord ar = pair.getLeftRecord();
			final EncodedRecord br = pair.getRightRecord();
		
			final JaccardSimilarity jaccard = new JaccardSimilarity();
			final StrictOverlapSimilarity overlap = new StrictOverlapSimilarity();
			
			double simJaccard = jaccard.calculateSimilarity(ar.getBitVectors().get(0), br.getBitVectors().get(0));
			double simOverlap = overlap.calculateSimilarity(ar.getBitVectors().get(0), br.getBitVectors().get(0));
		
//			System.out.println(simJaccard);
			
			simJaccard = new BigDecimal(simJaccard).setScale(2, RoundingMode.HALF_UP).doubleValue();
			simOverlap = new BigDecimal(simOverlap).setScale(2, RoundingMode.HALF_UP).doubleValue();
			
			if (simOverlap == 1d){
				statSimOverlapJaccard.addValue(simJaccard);
			}

			
//			System.out.println(simJaccard);
			
			if (simJaccard >= 0.6d) count++;
			
			final BitSet arNonFrequent = (BitSet) ar.getBitVectors().get(0).clone();
			arNonFrequent.and(nonFrequentBitMask);;
			
			final BitSet brNonFrequent = (BitSet) br.getBitVectors().get(0).clone();
			brNonFrequent.and(nonFrequentBitMask);;
			
			final double simNonFrequentBits = 
					jaccard.calculateSimilarity(arNonFrequent,brNonFrequent);
			
			
			if (simNonFrequentBits > simJaccard){
				nonFrequentSimGreaterThanNormalSimCounter++;
			}
			
			
			final BitSet arFrequent1Bits = (BitSet) ar.getBitVectors().get(0).clone();
			arFrequent1Bits.andNot(frequentOneBitMask);
			
			final BitSet brFrequent1Bits = (BitSet) br.getBitVectors().get(0).clone();
			brFrequent1Bits.andNot(frequentOneBitMask);;
			
			final double simFrequen1tBits = 
					jaccard.calculateSimilarity(arFrequent1Bits,brFrequent1Bits);
			
			if (simFrequen1tBits > simJaccard){
				frequent1SimGreaterThanNormalSimCounter++;
			}
			
			
			final BitSet arFrequent0Bits = (BitSet) ar.getBitVectors().get(0).clone();
			arFrequent0Bits.andNot(frequentZeroBitMask);
			
			final BitSet brFrequent0Bits = (BitSet) br.getBitVectors().get(0).clone();
			brFrequent0Bits.andNot(frequentZeroBitMask);;
			
			final double simFrequen0tBits = 
					jaccard.calculateSimilarity(arFrequent0Bits,brFrequent0Bits);
		
			statSimJaccard.addValue(simJaccard);
			statSimOverlap.addValue(simOverlap);		
			statSimNonFrequent.addValue(simNonFrequentBits);
			statSimFrequentOneBits.addValue(simFrequen1tBits);
			statSimFrequentZeroBits.addValue(simFrequen0tBits);
		}
		
		final int[] simJaccardHisto = calcSimJaccardHistogram(statSimJaccard, 0, 1.01, 20);
		final int[] simOverlapHisto = calcSimOverlapHistogram(statSimOverlap);
		
		final int[] simOverlapJaccardHisto = calcSimJaccardHistogram(statSimOverlapJaccard, 0, 1.01, 20);
		
		final int[] simNonFrequentHisto = calcSimJaccardHistogram(statSimNonFrequent, 0, 1.01, 20);
		final int[] simFrequentOneBitsHisto = calcSimJaccardHistogram(statSimFrequentOneBits, 0, 1.01, 20);
		final int[] simFrequentZeroBitsHisto = calcSimJaccardHistogram(statSimFrequentZeroBits, 0, 1.01, 20);
		
		System.out.println("Jaccard-Similarity: " + Arrays.toString(simJaccardHisto));
		System.out.println("Overlap-Similarity: " + Arrays.toString(simOverlapHisto));
		System.out.println("Jaccard-Similarity-Overlap :" +  Arrays.toString(simOverlapJaccardHisto));
		System.out.println("NonFrequentJaccard-Similarity" +  Arrays.toString(simNonFrequentHisto));
		System.out.println("FrequentOneBitsJaccard-Similarity" +  Arrays.toString(simFrequentOneBitsHisto));
		System.out.println("FrequentZeroBitsJaccard-Similarity" +  Arrays.toString(simFrequentZeroBitsHisto));
		
		System.out.println("Count:" + count);
		
		System.out.println("nonFrequentSimGreaterThanNormalSimCounter: " + nonFrequentSimGreaterThanNormalSimCounter);
		System.out.println("frequent1SimGreaterThanNormalSimCounter: " +  frequent1SimGreaterThanNormalSimCounter);
	}
	
	public static int[] calcSimJaccardHistogram(DescriptiveStatistics stats, double min, double max, int numBins) {
		final double[] data = stats.getValues();
		final int[] result = new int[numBins];
		final double binSize = (max - min)/(numBins);

		for (double d : data) {
			int bin = (int) ((d - min) / binSize);
		    if (bin < 0) { 
		    	/* this data is smaller than min */ 
		    }
		    else if (bin >= numBins) { 
		    	/* this data point is bigger than max */ 
		    }
		    else {
		      result[bin] += 1;
		    }
		}
		return result;
	}
	
	public static int[] calcSimOverlapHistogram(DescriptiveStatistics stats){
		final int[] histo = new int[2];
		for (double val : stats.getValues()){
			if (val == 0d){
				histo[0]++;
			}
			else{
				histo[1]++;
			}
		}
		return histo;
	}
	
	
	@SuppressWarnings("unused")
	private static Integer[] determineFrequentBitPositions(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB){				
		final Map<Integer, Long> bitPositionsMap = 
					getBitPositionFrequency(recordsPartyA, recordsPartyB);
		
		final Integer[] sortedBitPositions = 
					getBitPositionsSortedByFrequency(bitPositionsMap);
							
		final Integer[] nonFrequentBitPositions =
			getNonFrequentBitPositions(sortedBitPositions);
		
		return nonFrequentBitPositions;
	}
	
	private static Map<Integer, Long> getBitPositionFrequency(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB){
		final Map<Integer, Long> posMap  = Stream
			.concat(
				recordsPartyA
					.stream(),
				recordsPartyB
					.stream())
			.flatMapToInt(record -> 
				record
					.getBitVectors().get(0)
					.stream()
					.parallel())
			.boxed()
			.collect(
				Collectors
					.groupingBy(
						Function.identity(),
						Collectors.counting()
					)
			);
		return posMap;
	}
	
	private static Integer[] getBitPositionsSortedByFrequency(List<EncodedRecord> recordsPartyA, List<EncodedRecord> recordsPartyB){
		final Map<Integer, Long> bitPositionsMap = 
				getBitPositionFrequency(recordsPartyA, recordsPartyB);
		System.out.println(bitPositionsMap);
		return getBitPositionsSortedByFrequency(bitPositionsMap);
	}
	
	
	private static Integer[] getBitPositionsSortedByFrequency(Map<Integer, Long> bitPositionsMap){
		return bitPositionsMap
			.entrySet()
			.stream()
			.sorted(Map.Entry.comparingByValue())
			.map(Entry::getKey)
			.toArray(Integer[]::new);
	}

	
	private static Integer[] getNonFrequentBitPositions(Integer[] sortedBitPositions){
		final int bfLength = 1024;
		
		final org.apache.commons.math3.fraction.Fraction pruningProportion = new org.apache.commons.math3.fraction.Fraction(1, 8);
		final int startIndex = pruningProportion.multiply(bfLength).intValue();
		final int endIndex = bfLength - startIndex; 
		
		return Arrays.copyOfRange(sortedBitPositions, startIndex, endIndex);
	}
	
	private static Integer[] getFrequentZeroBitPositions(Integer[] sortedBitPositions){
		final int bfLength = 1024;
		
		final org.apache.commons.math3.fraction.Fraction pruningProportion = new org.apache.commons.math3.fraction.Fraction(1, 8);
		final int startIndex = 0;
		final int endIndex = pruningProportion.multiply(bfLength).intValue();		
		return Arrays.copyOfRange(sortedBitPositions, startIndex, endIndex);
	}	
	
	private static Integer[] getFrequentOneBitPositions(Integer[] sortedBitPositions){
		final int bfLength = 1024;
		
		final org.apache.commons.math3.fraction.Fraction pruningProportion = new org.apache.commons.math3.fraction.Fraction(1, 8);
		final int startIndex = pruningProportion.multiply(bfLength).intValue();
		final int endIndex = bfLength - startIndex; 
		
		return Arrays.copyOfRange(sortedBitPositions, endIndex, 1024);
	}	

	
	public static void main(String[] args) throws IOException{
		final String pathA = "/home/mfranke/Schreibtisch/N1_dup_2_1024.csv";
		final List<EncodedRecord> encodedRecordsA = readRecordsFromFile(pathA);	
		System.out.println("Read records party A done. #Records: " + encodedRecordsA.size());
		
		final String pathB = "/home/mfranke/Schreibtisch/N1_org_2_1024.csv";
		final List<EncodedRecord> encodedRecordsB = readRecordsFromFile(pathB);
		System.out.println("Read records party B done. #Records: " + encodedRecordsB.size());
		
	
		final Integer[] sortedBits = getBitPositionsSortedByFrequency(encodedRecordsA, encodedRecordsB);
	    final Integer[] nonFrequentBits = getNonFrequentBitPositions(sortedBits);
		final Integer[] frequenOneBits = getFrequentOneBitPositions(sortedBits);
	    final Integer[] frequenZeroBits = getFrequentZeroBitPositions(sortedBits);
	    
	    System.out.println(nonFrequentBits.length);
	    System.out.println(frequenOneBits.length);
	    System.out.println(frequenZeroBits.length);
	    
	    final BitSet nonFrequentBitMask = new BitSet(nonFrequentBits.length);
	    for (final Integer i : nonFrequentBits){
	    	nonFrequentBitMask.set(i);
	    }
	    
	    final BitSet frequentOneBitMask = new BitSet(frequenOneBits.length);
	    for (final Integer i : frequenOneBits){
	    	frequentOneBitMask.set(i);
	    }
	    
	    final BitSet frequentZeroBitMask = new BitSet(frequenZeroBits.length);
	    for (final Integer i : frequenZeroBits){
	    	frequentZeroBitMask.set(i);
	    }
		
	    final List<CandidatePair> matches = getMatches(encodedRecordsA, encodedRecordsB);
	    System.out.println("#Matches: " + matches.size());
	    determineSimilarities(matches, true, nonFrequentBitMask, frequentOneBitMask, frequentZeroBitMask);
		
	    final List<CandidatePair> nonMatches = getNonMatches(encodedRecordsA, encodedRecordsB);
	    determineSimilarities(nonMatches, false, nonFrequentBitMask, frequentOneBitMask, frequentZeroBitMask);
	}

}