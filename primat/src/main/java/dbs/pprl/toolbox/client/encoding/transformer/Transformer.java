package dbs.pprl.toolbox.client.encoding.transformer;

import java.util.Set;

import dbs.pprl.toolbox.client.encoding.attributes.Attribute;


public abstract class Transformer {

	public abstract Set<String> transform(Attribute<?> attrValue); 
	
}
