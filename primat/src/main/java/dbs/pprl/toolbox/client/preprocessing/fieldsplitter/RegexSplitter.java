package dbs.pprl.toolbox.client.preprocessing.fieldsplitter;

public class RegexSplitter implements Splitter{
	
	private final String regex;
	private final int parts;

	public RegexSplitter(String regex, int parts) {
		this.regex = regex;
		this.parts = parts;
	}
	
	@Override
	public String[] split(String string) {
		return string.split(this.regex, this.parts);
	}

	@Override
	public int parts() {
		return this.parts;
	}
}