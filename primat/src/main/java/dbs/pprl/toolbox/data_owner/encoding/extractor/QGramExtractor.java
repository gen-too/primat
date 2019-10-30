package dbs.pprl.toolbox.data_owner.encoding.extractor;

import java.util.LinkedHashSet;

import dbs.pprl.toolbox.data_owner.data.attributes.Attribute;

public class QGramExtractor extends FeatureExtractor{

	private static final String PADDING_CHARACTER = "#";
	
	private int q;
	private boolean characterPadding;
	
	public QGramExtractor(int q) {
		this(q, false);
	}
	
	public QGramExtractor(int q, boolean characterPadding) {
		if (q < 1 || q > 4){
			throw new RuntimeException();
		}
		else{
			this.q = q;
			this.characterPadding = characterPadding;
		}
	}
	
	private String padString(String value){
		String result = value;
		
		for (int i = 1; i < this.q; i++){
			result = PADDING_CHARACTER + result + PADDING_CHARACTER;
		}
		
		return result;
	}
	
	@Override
	public LinkedHashSet<String> extract(Attribute<?> attr) {
		String value = attr.getStringValue();
		
		if (this.characterPadding){
			value = this.padString(value);
		}
		
		final char[] chars = value.toCharArray();
		final LinkedHashSet<String> tokens = new LinkedHashSet<String>();
		
		String token = "";
		
		for (int i = 0; i <= chars.length - this.q; i++){
			for (int j = i; j < i + this.q; j++){
				token = token + chars[j];
			}
			
			tokens.add(token);
			token = "";
		}
		
		return tokens;		
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}