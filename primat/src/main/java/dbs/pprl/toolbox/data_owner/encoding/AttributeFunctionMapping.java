package dbs.pprl.toolbox.data_owner.encoding;

import java.util.Map;

import dbs.pprl.toolbox.data_owner.data.attributes.Attribute;

public class AttributeFunctionMapping<T> {

	Map<Attribute<T>, Attribute<T>> mapFunc;
	
	public void add(Attribute<T> attr, Attribute<T> attr2){
		mapFunc.put(attr, attr2);
	}
	
	
}
