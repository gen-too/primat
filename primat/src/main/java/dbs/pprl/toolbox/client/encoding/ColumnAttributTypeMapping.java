package dbs.pprl.toolbox.client.encoding;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import dbs.pprl.toolbox.client.encoding.attributes.AttributeType;

public class ColumnAttributTypeMapping {

	private final int idColumn;
	
	private Map<Integer, AttributeType> mapping;
	
	public ColumnAttributTypeMapping(int idColumn){
		this.idColumn = idColumn;
		this.mapping = new TreeMap<Integer, AttributeType>();
		this.mapping.put(idColumn, AttributeType.ID);
	}
		
	private boolean isColumnMapped(int column){
		return this.mapping.containsKey(column);
	}
	
	public void setType(int column, AttributeType type){
		if (type == AttributeType.ID){
			throw new RuntimeException("AttributeColumnOverwriteException");
		}
		
		if (isColumnMapped(column)){
			throw new RuntimeException("AttributeColumnOverwriteException");
		}
		
		this.mapping.put(column, type);
	}
	
	public void setTypeString(int column){
		this.setType(column, AttributeType.STRING);
	}
	
	public void setTypeDate(int column){
		this.setType(column, AttributeType.DATE);
	}
	
	public void setTypeNumeric(int column){
		this.setType(column, AttributeType.NUMERIC);
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
	
	public Map<Integer, AttributeType> getMappingWithoutIdColumn(){
		final Map<Integer, AttributeType> res = new HashMap<>(this.mapping);
		res.remove(idColumn);
		return res;
	}
}