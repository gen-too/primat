package dbs.pprl.toolbox.lu.blocking;

import dbs.pprl.toolbox.lu.matching.EncodedRecord;

/**
 * 
 * @author mfranke
 *
 */
public class CandidatePair {
	//TODO: Consider to use Pair- or Tuple-Objects here or wrap them in the corresponding class
	//TODO: What's with a party / dataset id ?
	/*
	 * TODO: How is this extensible to a multi-party setting?
	 * MP: Now we have CandidateTuples, i. e. we need n-tuples (n>=2) instead of pairs (n=2)
	 * In general tuples are an ordered sequence of n-elements
	 */
	protected EncodedRecord leftRecord;
	protected EncodedRecord rightRecord;
	
	public CandidatePair(){
		this.leftRecord = null;
		this.rightRecord = null;
	}
	
	public CandidatePair(EncodedRecord leftRecord, EncodedRecord righRecord){
		this.leftRecord = leftRecord;
		this.rightRecord = righRecord;
	}

	public EncodedRecord getLeftRecord() {
		return leftRecord;
	}
	
	public EncodedRecord getRecord(Side side){
		if (side == Side.LEFT){
			return leftRecord;
		}
		else if (side == Side.RIGHT){
			return rightRecord;
		}
		else{
			return null;
		}
	}
	
	public void setLeftRecord(EncodedRecord leftRecord) {
		this.leftRecord = leftRecord;
	}

	public EncodedRecord getRightRecord() {
		return rightRecord;
	}

	public void setRightRecord(EncodedRecord rightRecord) {
		this.rightRecord = rightRecord;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CandidatePair [leftRecord=");
		builder.append(leftRecord.getId());
		builder.append(", rightRecord=");
		builder.append(rightRecord.getId());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leftRecord == null) ? 0 : leftRecord.hashCode());
		result = prime * result + ((rightRecord == null) ? 0 : rightRecord.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CandidatePair)) {
			return false;
		}
		final CandidatePair other = (CandidatePair) obj;
		if (leftRecord == null) {
			if (other.leftRecord != null) {
				return false;
			}
		}
		else if (!leftRecord.equals(other.leftRecord)) {
			return false;
		}
		if (rightRecord == null) {
			if (other.rightRecord != null) {
				return false;
			}
		} 
		else if (!rightRecord.equals(other.rightRecord)) {
			return false;
		}
		return true;
	}	
	
}