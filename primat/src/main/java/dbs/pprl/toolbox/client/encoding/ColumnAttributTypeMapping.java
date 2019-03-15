package dbs.pprl.toolbox.client.encoding;

import java.util.Map;
import java.util.TreeMap;

import dbs.pprl.toolbox.client.data.attributes.AttributeType;

public class ColumnAttributTypeMapping {

	private final int idColumn;
	
	private Map<Integer, AttributeType> mapping;
	
	public ColumnAttributTypeMapping(int idColumn){
		this.idColumn = idColumn;
		this.mapping = new TreeMap<Integer, AttributeType>();
		this.mapping.put(idColumn, AttributeType.ID);
	}
		
	public boolean isColumnMapped(int column){
		return this.mapping.containsKey(column);
	}
	
	public ColumnAttributTypeMapping setType(int column, AttributeType type){
		if (type == AttributeType.ID){
			throw new RuntimeException("AttributeColumnOverwriteException");
		}
		
		if (isColumnMapped(column)){
			throw new RuntimeException("AttributeColumnOverwriteException");
		}
		
		this.mapping.put(column, type);
		return this;
	}
	
	public ColumnAttributTypeMapping setTypeString(int column){
		return this.setType(column, AttributeType.STRING);
	}
	
	public ColumnAttributTypeMapping setTypeDate(int column){
		return this.setType(column, AttributeType.DATE);
	}
	
	public ColumnAttributTypeMapping setTypeNumeric(int column){
		return this.setType(column, AttributeType.NUMERIC);
	}
	
	public AttributeType getType(int column){
		return this.mapping.get(column);
	}
	
	public int getIdColumn(){
		return idColumn;
	}
	
	public Map<Integer, AttributeType> getMapping(){
		return this.mapping;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(mapping.entrySet().toString());
		return builder.toString();
	}
	
	
}