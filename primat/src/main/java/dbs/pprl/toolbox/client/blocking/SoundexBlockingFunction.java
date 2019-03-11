package dbs.pprl.toolbox.client.blocking;

import org.apache.commons.codec.language.Soundex;

public final class SoundexBlockingFunction extends PhoneticBlockingFunction{

	public SoundexBlockingFunction() {
		super(new Soundex());
	}

}