package dbs.pprl.toolbox.client.encoding.attributes;

public class NumericAttribute extends Attribute<Integer>{

	public NumericAttribute(){
		super();
	}
	
	public NumericAttribute(Integer value){
		super(value);
	}
	
	@Override
	public void setValue(String value) {
		if (value != null && !value.isEmpty()){
			this.value = Integer.parseInt(value);
		}
		else{
			this.value = null;
		}
	}
}