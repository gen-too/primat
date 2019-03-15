package dbs.pprl.toolbox.client.data.attributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class DateAttribute extends Attribute<Date>{

	public DateAttribute(){
		super();
	}
	
	public DateAttribute(Date date){
		super(date);
	}
	
	@Override
	public void setValue(String value) throws AttributeParseException {
		if (value == null || value.isEmpty()){
			this.value = null;
		}
		else{
			try {
				final DateFormat f = DateFormat.getDateInstance();
				final Date date = f.parse(value);
				this.value = date;
			} 
			catch (ParseException e) {
				throw new AttributeParseException();
			}
		}
	}
}