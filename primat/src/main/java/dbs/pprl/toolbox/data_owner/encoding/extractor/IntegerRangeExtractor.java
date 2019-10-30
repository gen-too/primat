package dbs.pprl.toolbox.data_owner.encoding.extractor;

import java.util.LinkedHashSet;

import dbs.pprl.toolbox.data_owner.data.attributes.Attribute;
import dbs.pprl.toolbox.data_owner.data.attributes.NumericAttribute;

public class IntegerRangeExtractor extends FeatureExtractor{

	private final int rangeStart;
	private final int rangeEnd;
	private final int range;
	
	public IntegerRangeExtractor(int rangeStart, int rangeEnd, int range) {
		this.rangeStart = rangeStart;
		this.rangeEnd = rangeEnd;
		this.range = range;
	}
	
	@Override
	public LinkedHashSet<String> extract(Attribute<?> attrValue) {		
		if (attrValue instanceof NumericAttribute){
			final NumericAttribute numAttr = (NumericAttribute) attrValue;
			final Integer number = numAttr.getValue().intValue();
			
			final LinkedHashSet<String> res = new LinkedHashSet<>();
			res.add(number.toString());
			
			for (int i = 1; i <= this.range; i++){
				final Integer positive = number + i;
				if (positive > this.rangeEnd){
					final Integer postive2 = this.rangeStart + i - 1;
					res.add(postive2.toString());
				}
				else{
					res.add(positive.toString());
				}
				
				final Integer negative = number - i;
				if (negative < this.rangeStart){
					final Integer negative2 = this.rangeEnd - i + 1;
					res.add(negative2.toString());
				}
				else{
					res.add(negative.toString());
				}
			}
			return res;
		}
		else {
			return null;
		}
	}

}
