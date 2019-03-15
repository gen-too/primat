package dbs.pprl.toolbox.client.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import dbs.pprl.toolbox.client.encoding.ColumnAttributTypeMapping;

/**
 * Creates an object that determines the path and basic structure of a csv-file.
 * 
 * @author mfranke
 *
 */

public class CSVInputFile {
	
	private final String pathToFile;
	
	private final boolean hasHeader;
	
	private final ColumnAttributTypeMapping columnAttrMapping;
	
	public CSVInputFile(String pathToFile, boolean hasHeader, ColumnAttributTypeMapping columnAttrMapping) {
		this.pathToFile = pathToFile;
		this.hasHeader = hasHeader;
		this.columnAttrMapping = columnAttrMapping;
	}
	
	public String getPathToFile() {
		return pathToFile;
	}

	public boolean hasHeader() {
		return hasHeader;
	}

	public ColumnAttributTypeMapping getColumnAttributTypeMapping() {
		return columnAttrMapping;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CSVInputFile [pathToFile=");
		builder.append(pathToFile);
		builder.append(", hasHeader=");
		builder.append(hasHeader);
		builder.append(", columnAttrMapping=");
		builder.append(columnAttrMapping);
		builder.append("]");
		return builder.toString();
	}
}