package dbs.pprl.toolbox.data_owner.common.config;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import dbs.pprl.toolbox.data_owner.blocking.functions.BlockingFunction;
import dbs.pprl.toolbox.data_owner.blocking.methods.BlockingKey;
import dbs.pprl.toolbox.utils.ClassNameObjectConverter;


@Configuration
@PropertySource("blocking.properties")
@ConfigurationProperties
@Import(CSVInputFileConfig.class)
public class BlockingConfig {
	
	@Autowired
	private CSVInputFileConfig csvInputFile;
	
	private List<BlockingKeyConfig> bk;
	
	
	
	public CSVInputFileConfig getCsvInputFileConfig() {
		return this.csvInputFile;
	}

	public void setCsvInputFileConfig(CSVInputFileConfig csvInputFileConfig) {
		this.csvInputFile = csvInputFileConfig;
	}
	
	public List<BlockingKeyConfig> getBk(){
		return this.bk;
	}
	
	public void setBk(List<BlockingKeyConfig> list) {
		this.bk = list;
	}
	
	public List<BlockingKey> getBlockingKeys() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException{
		final List<BlockingKey> blockingKeys = new ArrayList<BlockingKey>(bk.size());
		
		for (final BlockingKeyConfig bkConf : this.bk) {
			final List<BlockingKeyDefConfig> comps = bkConf.getFunction();
			
			for (final BlockingKeyDefConfig bkDef : comps) {
				final Integer col = bkDef.getColumn();
				final String clazzName = bkDef.getName();
				final List<String> paramList = bkDef.getParams();
				final String[] params;
				if (paramList == null || paramList.size() == 0) {
					params = new String[0];
				}
				else {
					params = paramList.toArray(new String[paramList.size()]);
				}
			
				final Object obj = ClassNameObjectConverter.getObject("dbs.pprl.toolbox.client.blocking.functions", clazzName, params);
				final BlockingFunction func = (BlockingFunction) obj;
				final BlockingKey bk = new BlockingKey().set(col, func);
				blockingKeys.add(bk);
				
			}
		}
		
		return blockingKeys;
	}
	
    @Override
    public String toString() {
    	final StringBuilder sb = new StringBuilder();
    	sb.append("[");
    	for (final BlockingKeyConfig b : bk) {
    		sb.append(b);
    		sb.append(",");
    	}
    	sb.deleteCharAt(sb.length()-1);
    	sb.append("]");
    	return sb.toString();
    }
}