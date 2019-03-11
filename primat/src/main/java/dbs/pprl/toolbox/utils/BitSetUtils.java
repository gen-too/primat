package dbs.pprl.toolbox.utils;

import java.util.Base64;
import java.util.BitSet;


/**
 * 
 * @author mfranke
 *
 */
public final class BitSetUtils {

	private BitSetUtils(){
		throw new RuntimeException();
	}
	
	public static BitSet fromBase64(String string){
		final byte[] bytes = Base64.getDecoder().decode(string);
		return BitSet.valueOf(bytes);
//		return fromByteArray(bytes);
	}
	
	public static String toBase64(BitSet bitset){
		return Base64.getEncoder().encodeToString(bitset.toByteArray());
//		return Base64.getEncoder().encodeToString(toByteArray(bitset));
	}
	
	
	@Deprecated
	public static byte[] toByteArray(BitSet bits) {
		final byte[] bytes = new byte[(bits.length() + 7) / 8];
	    for (int i = 0; i < bits.length(); i++) {
	        if (bits.get(i)) {
	            bytes[bytes.length-i/8-1] |= 1<<(i%8);
	        }
	    }
	    return bytes;
	}
	
	@Deprecated
	public static BitSet fromByteArray(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[i / 8] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }
	
	public static BitSet fromBinaryReverse(String binary) {
	    final BitSet bitset = new BitSet(binary.length());
	    for (int i = 0; i < binary.length(); i++) {
	        if (binary.charAt(i) == '1') {
	            bitset.set(i);
	        }
	    }
	    return bitset;
	}
	
	public static BitSet fromBinary(String binary) {
	    final BitSet bitset = new BitSet(binary.length());
	    for (int i = 0; i < binary.length(); i++) {
	        if (binary.charAt(i) == '1') {
	            bitset.set(i);
	        }
	    }
	    return bitset;
	}
	
	public static String toBinary(BitSet bitset){
		StringBuilder result = new StringBuilder();
		
		for(int bitIndex = bitset.size(); bitIndex >= 0; bitIndex--){
			if (bitset.get(bitIndex)){
				result.append("1");
			}
			else{
				result.append("0");
			}
		}
		return result.toString();
	}
	
	public static String toBinaryReverse(BitSet bitset){
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

	@Deprecated
	public static BitSet from(String bits){		
		final BitSet bitset = new BitSet(bits.length());
		final int lastBitIndex = bits.length()-1;
		
		for (int i = lastBitIndex; i >= 0; i--){
			if(bits.charAt(i) == '1'){
				bitset.set(lastBitIndex - i); 
			}
		}
		
		return bitset;
	}
	
	public static BitSet and(BitSet first, BitSet second){	
		if (first != null && second != null && first.size() == second.size()){
			BitSet bitset = (BitSet) first.clone();
			bitset.and(second);
			return bitset;
		}
		else{
			return null;
		}
	}
	
	public static BitSet or(BitSet first, BitSet second){	
		if (first != null && second != null && first.size() == second.size()){
			BitSet bitset = (BitSet) first.clone();
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