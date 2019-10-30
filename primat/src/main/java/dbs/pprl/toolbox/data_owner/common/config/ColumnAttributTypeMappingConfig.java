package dbs.pprl.toolbox.data_owner.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import dbs.pprl.toolbox.data_owner.data.attributes.AttributeType;

@Configuration
public class ColumnAttributTypeMappingConfig {

	
	private Map<Integer, AttributeType> attrTypeMapping;

	public Map<Integer, AttributeType> getMapping() {
		return attrTypeMapping;
	}

	@Value("#{${columnTypes}}")	
	private void setMappingFromString(Map<Integer, String> mapping) {
		this.attrTypeMapping = new HashMap<>();	
		for (final Entry<Integer, String> entry : mapping.entrySet()) {
			final Integer column = entry.getKey();
			final AttributeType type = AttributeType.from(entry.getValue());
			this.setMapping(column, type);
		}
	}
	
	public void setMapping(Map<Integer, AttributeType> mapping) {
		this.attrTypeMapping = mapping;
	}
	
	public void setMapping(Integer column, AttributeType type) {
		this.attrTypeMapping.put(column, type);
	}
	
	public void setTypeString(Integer column) {
		this.attrTypeMapping.put(column, AttributeType.STRING);
	}
}