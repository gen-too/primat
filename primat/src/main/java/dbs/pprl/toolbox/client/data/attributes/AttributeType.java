package dbs.pprl.toolbox.client.data.attributes;


import java.util.Date;

//TODO: Somehow merge this with Attribute<?>
public enum AttributeType {
	ID("ID"),
	STRING("String"),
	DATE("Date"), 
	NUMERIC("Numeric");
	
	private final String name;
	
	private AttributeType(String name) {
		this.name = name;
	}

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
		if (this == ID){
			return new IdAttribute();
		}
		else if (this == STRING){
			return new StringAttribute();
		}
		else if (this == DATE){
			return new DateAttribute();
		}
		else if (this == NUMERIC){
			return new NumericAttribute();
		}
		return null;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static AttributeType from(String arg0) {
		for (final AttributeType at : AttributeType.values()) {
			if (at.getName().equalsIgnoreCase(arg0)) {
				return at;
			}
		}
		return null;
	}

}