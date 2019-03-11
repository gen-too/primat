package dbs.pprl.toolbox.client.blocking;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import dbs.pprl.toolbox.utils.ClassNameObjectConverter;

public class ConfigLoaderBlocking {
	
	public static final String CONFIG = "blocking.properties";
	public static final String PATH = "/home/mfranke/workspace/toolbox-pprl/" + CONFIG;
	
	public static final String DIR = "dbs.pprl.toolbox.client.blocking";
	public static final String KEY = "bk";
	
	private final PropertiesConfiguration config;
		
	public ConfigLoaderBlocking() throws ConfigurationException{
		this(PATH);
	}
		
	public ConfigLoaderBlocking(String path) throws ConfigurationException{
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
	
	public List<BlockingKeyDefinition> load() throws Exception{
		final List<BlockingKeyDefinition> bkDefs = new ArrayList<BlockingKeyDefinition>(); 
		
		final String[] bks = this.config.getStringArray(KEY);
		
		if (bks == null || bks.length < 1){
			return bkDefs; 
		}
		else{
			for (final String bk : bks){
				final BlockingKeyDefinition bkDef = new BlockingKeyDefinition();
				final String[] subBks = bk.split("&");

				for (final String subBk : subBks){
					final String[] argsSubBk = subBk.split(",");

					if (argsSubBk != null && argsSubBk.length >= 2){
						final int column = Integer.parseInt(argsSubBk[0]);
						final String className = argsSubBk[1];
						final String[] args = Arrays.copyOfRange(argsSubBk, 2, argsSubBk.length);
						final Object obj = ClassNameObjectConverter.getObject(DIR, className, args);
						final BlockingFunction func = (BlockingFunction) obj;
						bkDef.set(column, func);
					}
					else{
						throw new IllegalArgumentException();
					}
				}
				bkDefs.add(bkDef);
			}
		}
				
		return bkDefs;
	}
	
		
	public static void main(String[] args) throws Exception{
		final ConfigLoaderBlocking loader = new ConfigLoaderBlocking();
		System.out.println(loader.load());
	}
	
}
