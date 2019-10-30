package dbs.pprl.toolbox.client.blocking.functions;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public abstract class PhoneticBlockingFunction implements BlockingFunction{

	private final StringEncoder codec;
	
	protected PhoneticBlockingFunction(StringEncoder codec){
		this.codec = codec;
	}
	
	@Override
	public String apply(String attribute) {		
		String encoding = "";
		
		try {
			encoding = this.codec.encode(attribute);
		} 
		catch (EncoderException e) {
			e.printStackTrace();
		}
		return encoding;
	}
}