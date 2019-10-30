package dbs.pprl.toolbox.data_owner.common.config;

import java.util.List;

public class BlockingKeyDefConfig {

	private Integer column;
	private String name;
	private List<String> params;
		
		
	public Integer getColumn() {
		return column;
	}
	
	public void setColumn(Integer column) {
		this.column = column;
	
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getParams() {
		return params;
	}
	
	public void setParams(List<String> params) {
		this.params = params;
	}
	
	@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			builder.append(column);
			builder.append(", ");
			builder.append(name);
			builder.append(", ");
			builder.append(params);
			builder.append("]");
			return builder.toString();
		}				
}