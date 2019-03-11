package dbs.pprl.toolbox.client.encoding.attributes;

public class StringAttribute extends Attribute<String>{

	public StringAttribute(){
		super();
	}
	
	public StringAttribute(String value){
		super(value);
	}
	
	@Override
	public void setValue(String value) {
		this.value = value;		
	}
}
