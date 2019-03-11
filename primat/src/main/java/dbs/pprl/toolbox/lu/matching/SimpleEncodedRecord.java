package dbs.pprl.toolbox.lu.matching;

import java.util.BitSet;

/**
 * 
 * @author mfranke
 *
 */
public class SimpleEncodedRecord {
/*
 * TODO: Blocking Keys are no longer necessary after the blocking step (right?), so
 * theoretically they can be pruned to free some memory
 */
	private String id;
	private BitSet bitVector;
	// No blocking keys
	
	public SimpleEncodedRecord(String id, BitSet bitVector) {
		super();
		this.id = id;
		this.bitVector = bitVector;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BitSet getBitVector() {
		return bitVector;
	}

	public void setBitVector(BitSet bitVector) {
		this.bitVector = bitVector;
	}	
}