package dbs.pprl.toolbox.client.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration2.ex.ConfigurationException;

import dbs.pprl.toolbox.client.blocking.BlockingTask;
import dbs.pprl.toolbox.client.blocking.functions.BlockingFunction;
import dbs.pprl.toolbox.client.blocking.methods.BlockingKey;
import dbs.pprl.toolbox.client.common.CSVInputFile;
import dbs.pprl.toolbox.utils.ClassNameObjectConverter;

public class ConfigLoaderBlocking extends ConfigLoader{
	
	public static final String DIR = "dbs.pprl.toolbox.client.blocking";
	public static final String KEY = "bk";
	
	public ConfigLoaderBlocking(String path) throws ConfigurationException{
		super(path);
	}
	
	public BlockingTask build() throws Exception {
		final CSVInputFile csvInputFile = this.getInputFile();
		final BlockingTask blockingTask =  new BlockingTask(csvInputFile);
		final List<BlockingKey> blockingKeys = this.getBlockingKeys();
		blockingTask.addBlocker(blockingKeys);
		return blockingTask;
	}
	
	private List<BlockingKey> getBlockingKeys() throws Exception{
		final List<BlockingKey> bkDefs = new ArrayList<BlockingKey>(); 
		
		final String[] bks = this.config.getStringArray(KEY);
		
		if (bks == null || bks.length < 1){
			return bkDefs; 
		}
		else{
			for (final String bk : bks){
				final BlockingKey bkDef = new BlockingKey();
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
}