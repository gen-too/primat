package dbs.pprl.toolbox.client.encoding.transformer;

import java.util.HashSet;
import java.util.Set;

import dbs.pprl.toolbox.client.encoding.attributes.Attribute;

public class QGramTransformer extends Transformer{

	private static final String PADDING_CHARACTER = "#";
	
	private int q;
	private boolean characterPadding;
	
	public QGramTransformer(int q) {
		this(q, false);
	}
	
	public QGramTransformer(int q, boolean characterPadding) {
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
	public Set<String> transform(Attribute<?> attr) {
		String value = attr.getStringValue();
		
		if (this.characterPadding){
			value = this.padString(value);
		}
		
		final char[] chars = value.toCharArray();
		final Set<String> tokens = new HashSet<String>();
		
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

}