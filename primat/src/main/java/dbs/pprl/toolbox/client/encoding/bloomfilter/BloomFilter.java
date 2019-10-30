package dbs.pprl.toolbox.client.encoding.bloomfilter;

import java.util.BitSet;
import java.util.Set;

import javax.management.RuntimeErrorException;


public class BloomFilter {

	private int size;
	private BitSet bitset;
	
	public BloomFilter(BloomFilter another){
		this.size = another.size;
		this.bitset = (BitSet) another.bitset.clone();
	}
	
	public BloomFilter(int size){
		this(size, new BitSet(size));
	}
	
	public BloomFilter(int size, BitSet bitset){
		this.size = size;
		this.bitset = bitset;
	}
	
	public void setPositions(Set<Integer> positions){
		for (final Integer pos : positions){
			this.setPosition(pos);
		}
	}
	
	public void setPosition(Integer pos){
		if (pos < size){
			this.bitset.set(pos);	
		}
		else {
			throw new RuntimeException("WTF");
		}
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
	
	public BitSet getBitVector(){
		return bitset;
	}
	
	public void setBitVector(BitSet s){
		this.bitset = s;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(bitset);
		builder.append("]");
		return builder.toString();
	}	
}