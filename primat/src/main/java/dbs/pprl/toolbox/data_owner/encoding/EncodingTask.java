package dbs.pprl.toolbox.data_owner.encoding;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import dbs.pprl.toolbox.data_owner.common.Task;
import dbs.pprl.toolbox.data_owner.common.config.EncodingConfig;
import dbs.pprl.toolbox.data_owner.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.data_owner.data.records.EncodedRecord;
import dbs.pprl.toolbox.data_owner.data.records.Record;

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