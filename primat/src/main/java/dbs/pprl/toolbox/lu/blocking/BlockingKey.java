package dbs.pprl.toolbox.lu.blocking;

/**
 * 
 * @author mfranke
 *
 */
public abstract class BlockingKey<T>{
	
	private long blockingKeyId;
	private T blockingKeyValue;
		
	protected BlockingKey(){
		this.blockingKeyId = 0;
		this.blockingKeyValue = null;
	}
	
	protected BlockingKey(long blockingKeyId, T blockingKeyValue){
		this.blockingKeyId = blockingKeyId;
		this.blockingKeyValue = blockingKeyValue;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (blockingKeyId ^ (blockingKeyId >>> 32));
		result = prime * result + ((blockingKeyValue == null) ? 0 : blockingKeyValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BlockingKey)) {
			return false;
		}
		BlockingKey<?> other = (BlockingKey<?>) obj;
		if (blockingKeyId != other.blockingKeyId) {
			return false;
		}
		if (blockingKeyValue == null) {
			if (other.blockingKeyValue != null) {
				return false;
			}
		} else if (!blockingKeyValue.equals(other.blockingKeyValue)) {
			return false;
		}
		return true;
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName() + " [blockingKeyId=");
		builder.append(blockingKeyId);
		builder.append(", blockingKeyValue=");
		builder.append(blockingKeyValue);
		builder.append("]");
		return builder.toString();
	}

	public long getBlockingKeyId() {
		return blockingKeyId;
	}
	
	public void setBlockingKeyId(long blockingKeyId) {
		this.blockingKeyId = blockingKeyId;
	}
	
	public T getBlockingKeyValue() {
		return blockingKeyValue;
	}
	
	public void setBlockingKeyValue(T blockingKeyValue) {
		this.blockingKeyValue = blockingKeyValue;
	}
}