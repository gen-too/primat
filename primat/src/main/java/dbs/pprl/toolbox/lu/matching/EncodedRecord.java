package dbs.pprl.toolbox.lu.matching;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import dbs.pprl.toolbox.lu.blocking.BlockingKey;
import dbs.pprl.toolbox.utils.BitSetUtils;

/**
 * 
 * @author mfranke
 *
 */
public class EncodedRecord {
	
	// TODO: Is it currently a problem if the same id occurs in both datasets ???
	// 		 if so, we need the partyId to distinguish
	//private String partyId
	private String id;
	private List<BitSet> bitVectors;
	private List<? extends BlockingKey<?>> blockingKeys;
	
	public EncodedRecord(String id){
		this(id, null, null);
	}
	
	public EncodedRecord(String id, List<BitSet> bitVectors, List<? extends BlockingKey<?>> blockingKeys) {
		super();
		this.id = id;
		this.bitVectors = bitVectors;
		this.blockingKeys = blockingKeys;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	//TODO: Reconsider to use only id here. Do we need a partyId?
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof EncodedRecord)) {
			return false;
		}
		EncodedRecord other = (EncodedRecord) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} 
		else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
//		builder.append("EncodedRecord [id=");
		builder.append(id);
		builder.append(", ");
//		builder.append(", bitVector=");
		for (final BitSet bs : this.bitVectors) {
			builder.append(BitSetUtils.toBase64LittleEndian(bs));
		}
//		builder.append(", blockingKeys=");
//		builder.append(blockingKeys);
//		builder.append("]");
		return builder.toString();
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public List<BitSet> getBitVectors() {
		return bitVectors;
	}
	
	public void setBitVector(BitSet bitVector) {
		this.bitVectors = new ArrayList<BitSet>();
		this.bitVectors.add(bitVector);
	}
	
	public void setBitVectors(List<BitSet> bitVectors) {
		this.bitVectors = bitVectors;
	}
	
	public List<? extends BlockingKey<?>> getBlockingKeys() {
		return blockingKeys;
	}
	
	public void setBlockingKeys(List<? extends BlockingKey<?>> blockingKeys) {
		this.blockingKeys = blockingKeys;
	}
}