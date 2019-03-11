package dbs.pprl.toolbox.client.encoding.transformer;

import java.util.HashSet;
import java.util.Set;

import dbs.pprl.toolbox.client.encoding.attributes.Attribute;

public class SubstringTransformer extends Transformer{

	private int beginIndex;
	private int endIndex;
	
	public SubstringTransformer(int beginIndex, int endIndex) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}
	
	private static boolean isEmptyString(String string){
		return string == null || string.length() == 0;
	}
	
	@Override
	public Set<String> transform(Attribute<?> attr) {
		final String attrValue = attr.getStringValue();
		
		final Set<String> result = new HashSet<>();
		
		if (isEmptyString(attrValue)){
			return result;
		}
		
		final int valueLength = attrValue.length();
		final int customEndIndex = valueLength < endIndex ? valueLength : endIndex;			
		final String substring = attrValue.substring(beginIndex, customEndIndex);

		result.add(substring);
		
		return result;
	}
}
