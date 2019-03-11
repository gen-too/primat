package dbs.pprl.toolbox.client.blocking;

import org.apache.commons.codec.language.ColognePhonetic;

public final class ColognePhoneticBlockingFunction extends PhoneticBlockingFunction{
 
	public ColognePhoneticBlockingFunction() {
		super(new ColognePhonetic());
	}
}
