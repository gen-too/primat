package dbs.pprl.toolbox.data_owner.common.config;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SplitDefinitionConfig {

	private String function;
	
	private List<String> params;
	
}