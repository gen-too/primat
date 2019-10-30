package dbs.pprl.toolbox.client.common.config;


import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import dbs.pprl.toolbox.client.data.attributes.AttributeType;


/**
 * Creates an object that determines the path and basic structure of a csv-file.
 * 
 * @author mfranke
 *
 */
@Configuration
@ConfigurationProperties
public class CSVInputFileConfig {

	private String file;
	
	private boolean header;
	
	private Map<Integer, AttributeType> columnType;
	
		
	public void setColumnType(Map<Integer, AttributeType> columnType) {
		this.columnType = columnType;
	}
	
	public Map<Integer, AttributeType> getColumnType(){
		return this.columnType;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public boolean getHeader() {
		return header;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}
}