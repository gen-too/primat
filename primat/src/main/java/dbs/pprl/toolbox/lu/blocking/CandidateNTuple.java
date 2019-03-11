package dbs.pprl.toolbox.lu.blocking;

import dbs.pprl.toolbox.lu.matching.EncodedRecord;

/**
 * 
 * @author mfranke
 *
 */
@Deprecated
public class CandidateNTuple {
/*
 * TODO: Currently unused. Necessary for PPRL-MP.
 */
	private final int n;
	private final EncodedRecord[] tuple;
	
	public CandidateNTuple(int n){
		this.n = n;
		this.tuple = new EncodedRecord[this.n];
	}
	
	private void checkBound(int i){
		if (i >= this.n){
			throw new RuntimeException();
		}
	}
	
	public EncodedRecord get(int i){
		this.checkBound(i);
		return tuple[i];
	}
	
	public void set(int i, EncodedRecord rec){
		this.checkBound(i);
		this.tuple[i] = rec;
	}
}