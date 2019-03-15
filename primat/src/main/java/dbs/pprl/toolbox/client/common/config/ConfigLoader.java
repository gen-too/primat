package dbs.pprl.toolbox.client.common.config;

import java.io.File;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import dbs.pprl.toolbox.client.common.CSVInputFile;
import dbs.pprl.toolbox.client.data.attributes.AttributeType;
import dbs.pprl.toolbox.client.encoding.ColumnAttributTypeMapping;

public class ConfigLoader {
	
	public static final String FILE_PATH = "file";
	public static final String ID_COLUMN = "idColumn";
	public static final String HAS_HEADER = "hasHeader";
	public static final String COL_MAPPING = "col";
	
	public static final boolean DEFAULT_HAS_HEADER = false;
	
	public static final String ATTRIBUTES_DIR = "dbs.pprl.toolbox.client.encoding.attributes";


	protected final PropertiesConfiguration config;
				
	/**
	 * 
	 * @param path Path to config file.
	 * @throws ConfigurationException
	 */
	public ConfigLoader(String path) throws ConfigurationException{
		final Configurations configs = new Configurations();
		this.config = configs.properties(new File(path));	
		/*
		final FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
		        new FileBasedConfigurationBuilder<PropertiesConfiguration>(
		                PropertiesConfiguration.class)
		                .configure(new Parameters().properties()
		                		.setFile(new File(path))
		                        .setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
		                        .setThrowExceptionOnMissing(false));
		this.config = builder.getConfiguration();
		*/
	}
	
	protected CSVInputFile getInputFile() {
		final String filePath = this.config.getString(FILE_PATH);
		final boolean hasHeader = this.config.getBoolean(HAS_HEADER, DEFAULT_HAS_HEADER);
		final int idCol = this.config.getInt(ID_COLUMN);
		final ColumnAttributTypeMapping colAttrMap = new ColumnAttributTypeMapping(idCol);
		
		final String[] columns = this.config.getStringArray(COL_MAPPING);
		for (final String colDef : columns) {
			final String[] colDefFields = colDef.split(",");
			if (colDefFields.length != 2) {
				throw new RuntimeException();
			}
			else {
				final int column = Integer.parseInt(colDefFields[0]);
				final String attrDef = colDefFields[1];
				final AttributeType attrType = AttributeType.from(attrDef);
				colAttrMap.setType(column, attrType);
			}
		}
		
		return new CSVInputFile(filePath, hasHeader, colAttrMap);
	}

	public static void main(String[] args) throws ConfigurationException {
		System.out.println(new ConfigLoader("preprocessing.properties").getInputFile());
	}
}
