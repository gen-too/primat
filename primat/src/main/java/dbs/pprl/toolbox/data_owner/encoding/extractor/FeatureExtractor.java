package dbs.pprl.toolbox.data_owner.encoding.extractor;

import java.util.LinkedHashSet;

import dbs.pprl.toolbox.data_owner.data.attributes.Attribute;


public abstract class FeatureExtractor {

	public abstract LinkedHashSet<String> extract(Attribute<?> attrValue); 
	
}
