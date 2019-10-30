package dbs.pprl.toolbox.data_owner.lisard.attributes;

import dbs.pprl.toolbox.data_owner.lisard.lookup.IndipendentLookup;

public final class IndipendentAttribute extends Attribute {
	
	private final IndipendentLookup lookup;

	public IndipendentAttribute(String attributeName, boolean isFamilyAttribute, IndipendentLookup lookup){
		super(attributeName, isFamilyAttribute);
		this.lookup = lookup;
	}
	
	@Override
	public String getValue(long seed) {
		this.lookup.setSeed(seed);
		return this.lookup.getValue();
	};
}