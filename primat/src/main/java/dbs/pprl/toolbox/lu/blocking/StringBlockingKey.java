package dbs.pprl.toolbox.lu.blocking;

/**
 * 
 * @author mfranke
 *
 */
public final class StringBlockingKey extends BlockingKey<String>{

	public StringBlockingKey(){
		super();
	}
	
	public StringBlockingKey(long blockingKeyId, String blockingKeyValue) {
		super(blockingKeyId, blockingKeyValue);
	}
}
