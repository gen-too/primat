package dbs.pprl.toolbox.lu.blocking;

import java.util.BitSet;

/**
 * 
 * @author mfranke
 *
 */
public final class BitVectorBlockingKey extends BlockingKey<BitSet> {

	public BitVectorBlockingKey(){
		super();
	}
	
	public BitVectorBlockingKey(long blockingKeyId, BitSet blockingKeyValue) {
		super(blockingKeyId, blockingKeyValue);
	}
}