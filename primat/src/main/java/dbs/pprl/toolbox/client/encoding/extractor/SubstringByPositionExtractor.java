package dbs.pprl.toolbox.client.encoding.extractor;

import java.util.LinkedHashSet;

import dbs.pprl.toolbox.client.data.attributes.Attribute;

public class SubstringByPositionExtractor extends FeatureExtractor{

	private int beginIndex;
	private int endIndex;
	
	public SubstringByPositionExtractor(int beginIndex, int endIndex) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}
	
	private static boolean isEmptyString(String string){
		return string == null || string.length() == 0;
	}
	
	@Override
	public LinkedHashSet<String> extract(Attribute<?> attr) {
		final String attrValue = attr.getStringValue();
		
		final LinkedHashSet<String> result = new LinkedHashSet<>();
		
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
