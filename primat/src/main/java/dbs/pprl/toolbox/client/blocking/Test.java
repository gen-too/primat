package dbs.pprl.toolbox.client.blocking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("test.properties")
public class Test {

	@Value("${key}")
	private String key;
	
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		System.out.println(t.getKey());
	}
}
