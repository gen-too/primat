package dbs.pprl.toolbox.client.preprocessing.preprocessors;

import java.util.LinkedList;
import java.util.List;

import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.records.Record;

/**
 * A Preprocessor takes as input plain-text records retrieved from a csv-file and 
 * applies a specific function to one or several of the records attributes.
 * 
 * @author mfranke
 *
 */
public interface Preprocessor {
	
	/**
	 * Applies a pre-processing function on each record.
	 * @param records List of {@link StringRecord} objects.
	 * @param header The record scheme or representation given by a list of attribute names.
	 * @throws AttributeParseException 
	 */
	public void apply(List<Record> records, LinkedList<String> header) throws AttributeParseException;

}