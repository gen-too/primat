package dbs.pprl.toolbox.client.encoding.attributes;


public abstract class Attribute<T> {
	
	protected String name;
	protected T value;
		
	public Attribute(){}
	
	public Attribute(T value){
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	@Deprecated
	public void setName(String name) {
		this.name = name;
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
	
	public abstract void setValue(String value);

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Attribute [name=");
		builder.append(name);
		builder.append(", value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}
}