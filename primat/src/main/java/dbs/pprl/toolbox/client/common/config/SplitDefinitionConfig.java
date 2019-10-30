package dbs.pprl.toolbox.client.common.config;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SplitDefinitionConfig {

	private String function;
	
	private List<String> params;
	
}