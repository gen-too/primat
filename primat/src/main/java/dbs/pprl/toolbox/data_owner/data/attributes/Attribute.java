package dbs.pprl.toolbox.data_owner.data.attributes;


public abstract class Attribute<T> {
	
	protected T value;
		
	public Attribute(){}
	
	public abstract Attribute<?> newInstance();
	
	
	public Attribute(T value){
		this.value = value;
	}
	
	public T getValue(){
		return value;
	}
	
	public String getStringValue(){
		return value.toString();
	}
	
	
	public void setValue(T value){
		this.value = value;
	}
	
	public abstract void setValue(String value) throws AttributeParseException;

	
	@Override
	public String toString() {
		return this.getStringValue();
	}
}