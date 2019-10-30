package dbs.pprl.toolbox.lu.blocking.metricSpace;

import java.util.ArrayList;

import dbs.pprl.toolbox.lu.matching.EncodedRecord;


public class RoutingNode implements Node, Comparable<RoutingNode>{
	
	private EncodedRecord features;
    private double coveringRadius;       
    private ChildNode distantNode;      
    private int sourceId;    
    private ArrayList<ChildNode> entries;           
    public double maxMean;
    
    public RoutingNode(){
    	this.features = null;
    	this.coveringRadius = 0;
    	this.entries = new ArrayList<ChildNode>();
    	this.maxMean = 0;
    }
    
    public RoutingNode(final EncodedRecord features) {
    	this();
    	this.features= features;
    }
    
    public RoutingNode(final EncodedRecord features, double coveringRadius) {
    	this(features);
    	this.coveringRadius= coveringRadius;
    }
    
    public EncodedRecord getFeatures() {
    	return this.features;
    }
    
    public void setFeatures(EncodedRecord features) {
    	this.features= features;
    }
    
    public int getPopCount() {
    	return this.features.getBitVectors().get(0).cardinality();
    }

    public double getCoveringRadius() {
        return this.coveringRadius;
    }
    
    public void setCoveringRadius(double radius) {
        this.coveringRadius = radius;
    }
   
    public String getId() { 
    	return features.getId();
    }
    
    public void setDistantNode(ChildNode node) {
    	distantNode= node;
    }
    
    public ChildNode getDistantNode() {
    	return distantNode;
    }

	
    public ArrayList<ChildNode> getEntries() {
        return entries;
    }
	
    public void setEntries(final ArrayList<ChildNode> entries) {
    	this.entries = entries;
    }
    

	public void addEntry(final ChildNode node) {
    	entries.add(node);
    }
	
    public int getSourceId() {
    	return sourceId;
    }
    
    public void setSourceId(int sourceId) {
    	this.sourceId= sourceId;
    }
	
    public String toString() {
		return getId();	
    }
    
	@Override
	public int compareTo(RoutingNode o) {
		if(getId() == o.getId()){
			return 0;
		}	
		else{
			return 1;
		}
	}

}

