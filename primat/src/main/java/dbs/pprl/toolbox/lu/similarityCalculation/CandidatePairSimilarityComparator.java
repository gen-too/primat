package dbs.pprl.toolbox.lu.similarityCalculation;

import java.util.Comparator;

public class CandidatePairSimilarityComparator implements Comparator<CandidatePairWithSimilarity>{

	@Override
	public int compare(CandidatePairWithSimilarity o1, CandidatePairWithSimilarity o2) {
		return o1.getSimilarity().compareTo(o2.getSimilarity()) * -1;
	}

}
