package dbs.pprl.toolbox.lu.similarityCalculation;

import java.util.Comparator;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

/**
 * 
 * @author mfranke
 *
 */
public class CandidatePairWithSimilarity extends CandidatePair{

	private double similarity;
	
	public CandidatePairWithSimilarity(){
		super();
	}
		
	public CandidatePairWithSimilarity(CandidatePair candidatePair, double similarity){
		this(
			candidatePair.getLeftRecord(), 
			candidatePair.getRightRecord(),
			similarity
		);
	}
	
	public CandidatePairWithSimilarity(EncodedRecord leftRecord, EncodedRecord rightRecord, double similarity){
		this.leftRecord = leftRecord;
		this.rightRecord = rightRecord;
		this.similarity = similarity;
	}

	public Double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CandidatePairWithSimilarity [similarity=");
		builder.append(similarity);
		builder.append(", leftRecord=");
		builder.append(leftRecord);
		builder.append(", rightRecord=");
		builder.append(rightRecord);
		builder.append("]");
		return builder.toString();
	}	
	
	class CandidatePairSimilarityComparator implements Comparator<CandidatePairWithSimilarity>{
		@Override
		public int compare(CandidatePairWithSimilarity o1, CandidatePairWithSimilarity o2) {
			return o1.getSimilarity().compareTo(o2.getSimilarity());
		}
	}
}