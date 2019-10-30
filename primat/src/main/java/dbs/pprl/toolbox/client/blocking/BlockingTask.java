package dbs.pprl.toolbox.client.blocking;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import dbs.pprl.toolbox.client.blocking.methods.BlockingKey;
import dbs.pprl.toolbox.client.blocking.methods.StandardBlocker;
import dbs.pprl.toolbox.client.common.Task;
import dbs.pprl.toolbox.client.common.config.BlockingConfig;
import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.attributes.StringAttribute;
import dbs.pprl.toolbox.client.data.records.Record;

@Service
@Import(BlockingConfig.class)
public class BlockingTask extends Task{

	public static final String TASK_NAME = "bk";
	
	final List<BlockingKey> bkList;
	
	@Autowired
	public BlockingTask(BlockingConfig blockingConfig) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException{
		super(blockingConfig.getCsvInputFileConfig());
		System.out.println(blockingConfig.getCsvInputFileConfig().getColumnType());
		System.out.println(blockingConfig.getCsvInputFileConfig().getFile());
		System.out.println(blockingConfig.getCsvInputFileConfig().getHeader());
		this.bkList = blockingConfig.getBlockingKeys();
	}
	
	@Override
	public String toString() {
		return this.csvInputFile.getFile() + ", " + this.bkList;
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
}