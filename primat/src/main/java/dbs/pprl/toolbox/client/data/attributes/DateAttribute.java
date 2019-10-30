package dbs.pprl.toolbox.client.data.attributes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateAttribute extends Attribute<LocalDate>{

	public DateAttribute(){
		super();
	}
	
	public DateAttribute(LocalDate date){
		super(date);
	}
	
	@Override
	public void setValue(String value) throws AttributeParseException {
		if (value == null || value.isEmpty()){
			this.value = null;
		}
		else{
			try {
				LocalDate date = LocalDate.parse(value);
				
				this.value = date;
			} 
			catch (DateTimeParseException e) {
				throw new AttributeParseException();
			}
		}
	}

	@Override
	public DateAttribute newInstance() {
		return new DateAttribute(this.getValue());
	}
}