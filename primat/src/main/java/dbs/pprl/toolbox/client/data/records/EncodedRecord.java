package dbs.pprl.toolbox.client.data.records;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import dbs.pprl.toolbox.client.data.Printable;
import dbs.pprl.toolbox.utils.BitSetUtils;

/**
 * 
 * @author mfranke
 *
 */
public class EncodedRecord implements Printable{
	
	private final String id;
	private List<BitSet> bitVectors;
	
	public EncodedRecord(String id){
		this(id, null);
	}
	
	public EncodedRecord(String id, List<BitSet> bitVectors) {
		super();
		this.id = id;
		this.bitVectors = bitVectors;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(id);
		for (final BitSet bs : this.bitVectors) {
			builder.append(", ");
			builder.append(BitSetUtils.toBase64LittleEndian(bs));
		}
		builder.append("]");
		return builder.toString();
	}

	public String getId() {
		return id;
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

	@Override
	public Iterable<?> getPrint() {
		final List<String> values = new ArrayList<>();
		values.add(this.id);
		for (final BitSet bs : this.bitVectors) {
			values.add(BitSetUtils.toBase64LittleEndian(bs));
		}
		return values;
	}
}