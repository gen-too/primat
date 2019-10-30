package dbs.pprl.toolbox.data_owner.data.attributes;

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

	@Override
	public StringAttribute newInstance() {
		return new StringAttribute(this.getValue());
	}
}
