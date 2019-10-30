package dbs.pprl.toolbox.data_owner.common.config;

import java.util.List;

public class BlockingKeyConfig {

	private List<BlockingKeyDefConfig> function;

	public List<BlockingKeyDefConfig> getFunction() {
		return function;
	}

	public void setFunction(List<BlockingKeyDefConfig> function) {
		this.function = function;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		
		for (final BlockingKeyDefConfig n : function) {
			builder.append(n);
			builder.append(",");
		}
		builder.deleteCharAt(builder.length()-1);
		builder.append("]");
		return builder.toString();
	}
	
}