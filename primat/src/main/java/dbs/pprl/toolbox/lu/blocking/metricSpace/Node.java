package dbs.pprl.toolbox.lu.blocking.metricSpace;

import dbs.pprl.toolbox.lu.matching.EncodedRecord;

public interface Node {
	
	public EncodedRecord getFeatures();
	
	public double getCoveringRadius();
	
	public String getId();
}