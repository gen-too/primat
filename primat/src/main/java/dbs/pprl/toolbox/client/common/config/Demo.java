package dbs.pprl.toolbox.client.common.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import dbs.pprl.toolbox.client.blocking.BlockingTask;

@SpringBootApplication
@ComponentScan("dbs.pprl.toolbox.client.blocking")
public class Demo implements CommandLineRunner{
	
	@Autowired
    private ApplicationContext ctx;
	
    public static void main(String[] args) {
        new SpringApplicationBuilder(Demo.class).headless(false).run(args);
    }
  
	@Override
	public void run(String... args) throws Exception {
		final BlockingTask blockingTask = this.ctx.getBean(BlockingTask.class);
		System.out.println(blockingTask.toString());
        
	}
}