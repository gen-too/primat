package dbs.pprl.toolbox.client.data.attributes;

import java.text.NumberFormat;
import java.text.ParseException;

public class NumericAttribute extends Attribute<Number>{

	public NumericAttribute(){
		super();
	}
	
	public NumericAttribute(Integer value){
		super(value);
	}
	
	@Override
	public void setValue(String value) throws AttributeParseException {
		if (value != null && !value.isEmpty()){	
			try {
				final Number number = NumberFormat.getInstance().parse(value);
				this.value = number;
			}
			catch (ParseException e) {
				throw new AttributeParseException();
			}
		}
		else {
			this.value = null;
		}
	}
}