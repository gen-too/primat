package dbs.pprl.toolbox.client.blocking;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.math3.util.Pair;

import dbs.pprl.toolbox.client.encoding.CSVRecordEntry;


public class BlockingKeyDefinition {

	public final String DELIMITER = "";
	
	private List<Pair<Integer, BlockingFunction>> columnFunctionMapping;
	
	
	public BlockingKeyDefinition() {
		this.columnFunctionMapping = new ArrayList<>();
	}
	
	public BlockingKeyDefinition set(int column, BlockingFunction func){
		this.columnFunctionMapping.add(new Pair<Integer, BlockingFunction>(column, func));
		return this;
	}
	
	public String getBlockingKey(CSVRecordEntry rec){
		final StringJoiner result = new StringJoiner(DELIMITER);
		for (final Pair<Integer, BlockingFunction> pair : columnFunctionMapping){
			final Integer column = pair.getKey();
			final String attrValue = rec.getAttribute(column);
			
			final BlockingFunction bkFunc = pair.getValue();
			final String bkValue = bkFunc.apply(attrValue);
			result.add(bkValue);
		}
		return result.toString();
	}
}
