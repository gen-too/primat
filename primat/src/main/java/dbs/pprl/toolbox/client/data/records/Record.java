package dbs.pprl.toolbox.client.data.records;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import dbs.pprl.toolbox.client.data.Printable;
import dbs.pprl.toolbox.client.data.attributes.Attribute;
import dbs.pprl.toolbox.client.data.attributes.IdAttribute;

public class Record implements Printable{
	
	private IdAttribute id;
	private List<Attribute<?>> attributes;
	
	public Record() {
		this(null, new ArrayList<Attribute<?>>());
	}
	
	public Record copy() {
		final Record copy = new Record();
		
		for (final Attribute<?> attr : this.getAttributes()) {
			
			final Attribute<?> attrCopy = attr.newInstance();
			copy.add(attrCopy);
		}
		return copy;
	}
	
	public Record(IdAttribute id, List<Attribute<?>> attributes){
		this.id = id;
		this.attributes = attributes;
	}
	
	public IdAttribute getIdAttribute() {
		return this.id;
	}
	
	public void checkIdAttribute(Attribute<?> attribute){
		if (attribute instanceof IdAttribute){
			if (this.id == null) {
				this.id = (IdAttribute) attribute;
			}
			else {
				throw new IllegalArgumentException();
			}
		}
	}
		
	/**
	 * Add attribute to the end;
	 */
	public void add(Attribute<?> attribute){
		this.checkIdAttribute(attribute);
		this.attributes.add(attribute);
	}
	
	public void add(int index, Attribute<?> attribute){
		this.checkIdAttribute(attribute);		
		this.attributes.add(index, attribute);
	}
	
	public void remove(int index) {
		final Attribute<?> attr = this.attributes.get(index);
		if (attr instanceof IdAttribute) {
			throw new IllegalArgumentException();
		}
		else {
			this.attributes.remove(index);
		}
	}
	
	public String getId() {
		return this.id.getValue();
	}
	
	public Attribute<?> getAttribute(int column){
		return this.attributes.get(column);
	}
	
	public List<Attribute<?>> getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		final StringJoiner result = new StringJoiner(",");
		for (final Attribute<?> att : this.attributes) {
			result.add(att.toString());
		}
		return result.toString();
	}

	@Override
	public Iterable<?> getPrint() {
		return this.attributes;
	}	
}