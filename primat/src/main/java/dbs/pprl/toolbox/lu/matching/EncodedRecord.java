package dbs.pprl.toolbox.lu.matching;

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
	private BitSet bitVector;
	private List<? extends BlockingKey<?>> blockingKeys;
	
	public EncodedRecord(){}
	
	public EncodedRecord(String id, BitSet bitVector, List<? extends BlockingKey<?>> blockingKeys) {
		super();
		this.id = id;
		this.bitVector = bitVector;
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
		builder.append(BitSetUtils.toBase64(bitVector));
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
	
	public BitSet getBitVector() {
		return bitVector;
	}
	
	public void setBitVector(BitSet bitVector) {
		this.bitVector = bitVector;
	}
	
	public List<? extends BlockingKey<?>> getBlockingKeys() {
		return blockingKeys;
	}
	
	public void setBlockingKeys(List<? extends BlockingKey<?>> blockingKeys) {
		this.blockingKeys = blockingKeys;
	}
}