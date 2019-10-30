package dbs.pprl.toolbox.client.common.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import dbs.pprl.toolbox.client.blocking.BlockingTask;
import dbs.pprl.toolbox.client.preprocessing.PreprocessingTask;

@SpringBootApplication
@ComponentScan("dbs.pprl.toolbox.client.preprocessing")
public class Demo2 implements CommandLineRunner{
	
	@Autowired
    private ApplicationContext ctx;
	
    public static void main(String[] args) {
        new SpringApplicationBuilder(Demo2.class).headless(false).run(args);
    }
  
	@Override
	public void run(String... args) throws Exception {
		final PreprocessingTask task = this.ctx.getBean(PreprocessingTask.class);
		System.out.println(task.toString());
        
	}
}