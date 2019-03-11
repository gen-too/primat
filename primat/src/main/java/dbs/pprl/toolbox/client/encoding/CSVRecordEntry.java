package dbs.pprl.toolbox.client.encoding;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class CSVRecordEntry {
	
	private String id;
	private List<String> attributes;

	public CSVRecordEntry(){
		this.id = null;
		this.attributes = new LinkedList<>();
	}
	
	public void addAttribute(String attribute){
		this.attributes.add(attribute);
	}
	
	public void addAttribute(int pos, String attribute){
		this.attributes.add(pos, attribute);
	}
	
	public String removeAttribute(int col) {
		return attributes.remove(col);
	}
	
	public List<String> getAttributes() {
		return attributes;
	}
	
	public String getAttribute(int pos){
		return this.attributes.get(pos);
	}
	
	public String setAttribute(int pos, String attribute){
		return this.attributes.set(pos, attribute);
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(String id){
		return this.id;
	}
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(",");
		for (final String attr : attributes){
			joiner.add(attr);
		}
		return joiner.toString();
	}	
}