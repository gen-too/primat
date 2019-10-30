package dbs.pprl.toolbox.data_owner.common.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MergeDefinitionConfig {
	
	private int[] columns;
	private String merger;
}
