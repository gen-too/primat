package dbs.pprl.toolbox.client.encoding;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import dbs.pprl.toolbox.client.common.Task;
import dbs.pprl.toolbox.client.common.config.EncodingConfig;
import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.data.records.EncodedRecord;

@Service
@Import(EncodingConfig.class)
public class EncodingTask extends Task {

	public static final String TASK_NAME = "enc";
	
	private final Encoder encoder;
	
	@Autowired
	public EncodingTask(EncodingConfig encodingConfig){
		super(encodingConfig.getCsvInputFileConfig());
		this.encoder = encodingConfig.getEncoder();
	}
	
	@Override
	public void execute() throws RuntimeException, IOException, AttributeParseException {
		final List<Record> records = this.readFile();
		final List<EncodedRecord> encodedRecs = this.encoder.encode(records);		
		this.writeEncodedFile(encodedRecs);		
	}

	@Override
	public String getTaskName() {
		return "enc";
	}
				

	public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException, AttributeParseException{
		final AnnotationConfigApplicationContext applicationContext = 
        		new AnnotationConfigApplicationContext(EncodingTask.class);
        final EncodingTask encodingTask = applicationContext.getBean(EncodingTask.class);
        System.out.println(encodingTask);
//        encodingTask.execute();        
        applicationContext.close();
	}
}