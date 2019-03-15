package dbs.pprl.toolbox.client.encoding.extractor;

import java.util.LinkedHashSet;

import dbs.pprl.toolbox.client.data.attributes.Attribute;


public abstract class FeatureExtractor {

	public abstract LinkedHashSet<String> extract(Attribute<?> attrValue); 
	
}
