package dbs.pprl.toolbox.utils;

import java.util.Base64;
import java.util.BitSet;

import org.apache.commons.lang3.ArrayUtils;


/**
 * 
 * @author mfranke
 *
 */
public final class BitSetUtils {

	private BitSetUtils(){
		throw new RuntimeException();
	}
	
	public static BitSet fromBase64LittleEndian(String string){
		final byte[] bytes = Base64.getDecoder().decode(string);
		return BitSet.valueOf(bytes);
	}
	
	public static String toBase64LittleEndian(BitSet bitset){
		return Base64.getEncoder().encodeToString(bitset.toByteArray());
	}
	
	public static BitSet fromBase64BigEndian(String string){
		final byte[] bytes = Base64.getDecoder().decode(string);
		ArrayUtils.reverse(bytes);
		return BitSet.valueOf(bytes);
	}
	
	public static String toBase64BigEndian(BitSet bitset){
		final byte[] byteArray = bitset.toByteArray();
		ArrayUtils.reverse(byteArray);
		return Base64.getEncoder().encodeToString(byteArray);
	}
	
	
	
	public static BitSet fromBinaryLittleEndian(String binary) {
	    final BitSet bitset = new BitSet(binary.length()-1);
	    for (int i = 0; i < binary.length(); i++) {
	        if (binary.charAt(i) == '1') {
	            bitset.set(i);
	        }
	    }
	    return bitset;
	}
	
	public static String toBinaryLittleEndian(BitSet bitset){
		StringBuilder result = new StringBuilder();
		
		for(int bitIndex = 0; bitIndex < bitset.size(); bitIndex++){
			if (bitset.get(bitIndex)){
				result.append("1");
			}
			else{
				result.append("0");
			}
		}
		return result.toString();
	}	
	
	public static BitSet fromBinaryBigEndian(String binary) {		
	    final BitSet bitset = new BitSet(binary.length()-1);
	    for (int i = 0 ; i < binary.length(); i++) {
	        if (binary.charAt(i) == '1') {
	            final int pos = binary.length() - (i + 1);
	            bitset.set(pos);
	        }
	    }
	    return bitset;
	}
	
	public static String toBinaryBigEndian(BitSet bitset){
		StringBuilder result = new StringBuilder();
		
		for(int bitIndex = bitset.size()-1; bitIndex >= 0; bitIndex--){
			if (bitset.get(bitIndex)){
				result.append("1");
			}
			else{
				result.append("0");
			}
		}
		return result.toString();
	}
	
	public static BitSet and(BitSet first, BitSet second){	
		if (first != null && second != null && first.size() == second.size()){
			final BitSet bitset = (BitSet) first.clone();
			bitset.and(second);
			return bitset;
		}
		else{
			return null;
		}
	}
	
	public static BitSet andNot(BitSet first, BitSet second){	
		if (first != null && second != null && first.size() == second.size()){
			final BitSet bitset = (BitSet) first.clone();
			bitset.andNot(second);
			return bitset;
		}
		else{
			return null;
		}
	}
	
	public static BitSet or(BitSet first, BitSet second){	
		if (first != null && second != null && first.size() == second.size()){
			final BitSet bitset = (BitSet) first.clone();
			bitset.or(second);
			return bitset;
		}
		else{
			return null;
		}
	}
	
	public static BitSet xor(BitSet first, BitSet second){	
		if (first != null && second != null && first.size() == second.size()){
			BitSet bitset = (BitSet) first.clone();
			bitset.xor(second);
			return bitset;
		}
		else{
			return null;
		}
	}
}