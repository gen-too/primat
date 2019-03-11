package dbs.pprl.toolbox.client.encoding;

import java.util.HashMap;
import java.util.Map;

import dbs.pprl.toolbox.client.encoding.attributes.Attribute;
//TODO: do we really need record and csvrecordentry ? which one we should use for input for blocking and postprocessing?
public class Record {
	
	private String id;
	private Map<Integer, Attribute<?>> attributes;
	
	public Record(){
		this.attributes = new HashMap<>();
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public void add(Integer column, Attribute<?> attribute){
		this.attributes.put(column, attribute);
	}
	
	public String getId() {
		return this.id;
	}
	
	public Attribute<?> getAttribute(String name){
		return this.attributes.get(name);
	}
	
	public Attribute<?> getAttributeByColumn(Integer column){
		return this.attributes.get(column);
	}
	
	public Map<Integer, Attribute<?>> getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Record [id=");
		builder.append(id);
		builder.append(", attributes=");
		builder.append(attributes);
		builder.append("]");
		return builder.toString();
	}
}