package dbs.pprl.toolbox.client.blocking.methods;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.math3.util.Pair;

import dbs.pprl.toolbox.client.blocking.functions.BlockingFunction;
import dbs.pprl.toolbox.client.data.records.Record;

/**
 * A blocking key is defined by List of {@link BlockingFunction}s each applied to one attribute.
 *  
 * @author mfranke
 *
 */
public class BlockingKey {

	public final String DELIMITER = "";
	
	private List<Pair<Integer, BlockingFunction>> columnFunctionMapping;
	
	public BlockingKey() {
		this.columnFunctionMapping = new ArrayList<>();
	}
	
	public BlockingKey set(int column, BlockingFunction func){
		this.columnFunctionMapping.add(new Pair<Integer, BlockingFunction>(column, func));
		return this;
	}
	
	public String getBlockingKeyValue(Record rec){
		final StringJoiner result = new StringJoiner(DELIMITER);
		for (final Pair<Integer, BlockingFunction> pair : this.columnFunctionMapping){
			final Integer column = pair.getKey();
			final String attrValue = rec.getAttribute(column).getStringValue();
			
			final BlockingFunction bkFunc = pair.getValue();
			final String bkValue = bkFunc.apply(attrValue);
			result.add(bkValue);
		}
		return result.toString();
	}
}