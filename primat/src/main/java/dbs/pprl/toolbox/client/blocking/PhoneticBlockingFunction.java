package dbs.pprl.toolbox.client.blocking;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class PhoneticBlockingFunction implements BlockingFunction{

	private final StringEncoder codec;
	
	public PhoneticBlockingFunction(StringEncoder codec){
		this.codec = codec;
	}
	
	@Override
	public String apply(Object attribute) {
		final String attrString = attribute.toString();
		
		String encoding = "";
		
		try {
			encoding = this.codec.encode(attrString);
		} 
		catch (EncoderException e) {
			e.printStackTrace();
		}
		return encoding;
	}

}
