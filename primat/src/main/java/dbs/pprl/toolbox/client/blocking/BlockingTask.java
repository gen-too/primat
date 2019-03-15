package dbs.pprl.toolbox.client.blocking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dbs.pprl.toolbox.client.blocking.methods.BlockingKey;
import dbs.pprl.toolbox.client.blocking.methods.StandardBlocker;
import dbs.pprl.toolbox.client.common.CSVInputFile;
import dbs.pprl.toolbox.client.common.Task;
import dbs.pprl.toolbox.client.common.config.ConfigLoaderBlocking;
import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.attributes.StringAttribute;
import dbs.pprl.toolbox.client.data.records.Record;

public class BlockingTask extends Task{

	public static final String TASK_NAME = "bk";
	public static final String CONFIG_FILE_NAME = "blocking.properties";
	public static final String PATH = "/home/mfranke/workspace/toolbox-pprl/" + CONFIG_FILE_NAME;
	
	final List<BlockingKey> bkList;
	
	public static BlockingTask fromConfig(String pathToConfig) throws Exception {
		final ConfigLoaderBlocking confLoader = new ConfigLoaderBlocking(pathToConfig);
		return confLoader.build();
	}
	
	public BlockingTask(CSVInputFile cSVInputFile){
		super(cSVInputFile);
		this.bkList = new ArrayList<BlockingKey>();
	}

	public void addBlocker(BlockingKey... bks){
		for (final BlockingKey bk : bks){
			bkList.add(bk);
		}
	}
	
	public void addBlocker(List<BlockingKey> bks){
		for (final BlockingKey bk : bks){
			bkList.add(bk);
		}
	}
		
	@Override
	public String getTaskName() {
		return TASK_NAME;
	}
	
	@Override
	public void execute() throws RuntimeException, IOException, AttributeParseException {
		final List<Record> records = this.readFile();

		final StandardBlocker sb = new StandardBlocker(bkList);
		final Map<Record, List<String>> blocks = sb.block(records);
		
		final List<Record> result = new ArrayList<>(blocks.size());
		
		for (final Entry<Record, List<String>> rec : blocks.entrySet()){
			final Record newEntry = new Record();
			
			final Record entry = rec.getKey();
			final List<String> bks = rec.getValue();
		
			newEntry.add(entry.getIdAttribute());
			
			for (final String bk : bks){
				newEntry.add(new StringAttribute(bk));
			}
			result.add(newEntry);			
		}
		this.writeFile(result);
	}
	
	public static void main(String[] args) throws Exception{
		final BlockingTask blockingTask = BlockingTask.fromConfig(PATH);
		blockingTask.execute();

		/* MANUAL DEFINITION
		// How to construct from config file?
		final BlockingKeyDefinition bk1Def = new BlockingKeyDefinition();
		bk1Def.set(2, new SoundexBlockingFunction());
		
		final BlockingKeyDefinition bk2Def = new BlockingKeyDefinition();
		bk2Def.set(1, new SoundexBlockingFunction());
		bk2Def.set(2, new SoundexBlockingFunction());
		
		blocking.addBlocker(bk1Def, bk2Def);
		*/	
	}	
}