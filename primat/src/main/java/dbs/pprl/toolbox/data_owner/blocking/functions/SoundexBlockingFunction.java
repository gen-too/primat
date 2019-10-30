package dbs.pprl.toolbox.data_owner.blocking.functions;

import org.apache.commons.codec.language.Soundex;

public final class SoundexBlockingFunction extends PhoneticBlockingFunction{

	public SoundexBlockingFunction() {
		super(new Soundex());
	}

}