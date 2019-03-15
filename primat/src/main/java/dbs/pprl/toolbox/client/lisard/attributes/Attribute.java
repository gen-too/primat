package dbs.pprl.toolbox.client.lisard.attributes;

public abstract class Attribute{
	
	protected final String attributeName;
	protected final boolean isFamilyAttribute;

	protected Attribute(String attributeName, boolean isFamilyAttribute){
		this.attributeName = attributeName;
		this.isFamilyAttribute = isFamilyAttribute;
	}
	
	public abstract String getValue(long seed);
	
	
	public boolean isFamilyAttribute(){
		return this.isFamilyAttribute;
	}
	
	public String getAttributeName() {
		return this.attributeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Attribute)) {
			return false;
		}
		Attribute other = (Attribute) obj;
		if (attributeName == null) {
			if (other.attributeName != null) {
				return false;
			}
		} 
		else if (!attributeName.equals(other.attributeName)) {
			return false;
		}
		return true;
	}
}