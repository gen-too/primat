package dbs.pprl.toolbox.client.encoding.attributes;


import java.util.Date;

public enum AttributeType {
	ID,
	STRING,
	DATE, 
	NUMERIC;

	public Class<?> getType(){
		if (this == ID || this == STRING){
			return String.class;
		}
		if (this == DATE){
			return Date.class;
		}
		if (this == NUMERIC){
			return Integer.class;
		}
		return null;
	}
		
	public Attribute<?> constructAttribute(){
		if (this == STRING){
			return new StringAttribute();
		}
		if (this == DATE){
			return null;
		}
		if (this == NUMERIC){
			return new NumericAttribute();
		}
		return null;
	}
}