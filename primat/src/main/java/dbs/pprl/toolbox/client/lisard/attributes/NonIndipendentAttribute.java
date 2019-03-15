package dbs.pprl.toolbox.client.lisard.attributes;


import java.util.ArrayList;
import java.util.List;

import dbs.pprl.toolbox.client.lisard.lookup.SimpleDependentBasicRandomLookup;

public class NonIndipendentAttribute extends Attribute{

	private Attribute dependentAttribute;
	private SimpleDependentBasicRandomLookup dependentLookup;
	private String valueOfDependentAttribute;
	
	public NonIndipendentAttribute(String attributeName, boolean isFamilyAttribute, Attribute dependentAttribute,
			SimpleDependentBasicRandomLookup dependentLookup) {
		super(attributeName, isFamilyAttribute);
		this.dependentAttribute = dependentAttribute;
		this.dependentLookup = dependentLookup;
		this.valueOfDependentAttribute = null;
	}
	
	public Attribute getDependentAttribute() {
		return this.dependentAttribute;
	}
	
	public List<Attribute> getDependentAttributes() {
		final List<Attribute> result = new ArrayList<Attribute>();
		result.add(this.dependentAttribute);
		return result;
	}

	public void setValueOfDependentAttribute(String value) {
		this.valueOfDependentAttribute = value;
	}

	@Override
	public String getValue(long seed) {
		if (this.valueOfDependentAttribute == null || this.valueOfDependentAttribute.isEmpty()) {
			throw new RuntimeException();
		}
		else {
			this.dependentLookup.setDependency(this.valueOfDependentAttribute);
			this.dependentLookup.setSeed(seed);
			return this.dependentLookup.getValue();
		}
	}	
}