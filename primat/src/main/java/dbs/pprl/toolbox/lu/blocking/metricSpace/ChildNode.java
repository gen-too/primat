package dbs.pprl.toolbox.lu.blocking.metricSpace;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.lu.similarityCalculation.JaccardSimilarityCalculator;

public class ChildNode implements Node, Comparable<ChildNode> {
	
	private EncodedRecord features;   
	private double distanceToParent;	
    private double coveringRadius;   
    private RoutingNode routing;   
    private double accumulatedDistance;    
    private int sourceId;    
    private int clusterId; 
    private int oldClusterId;    
    private String soundex;   
    private HashMap<ChildNode, Double> forwardSimilars;
    private Set<ChildNode> similars;
    private boolean toIgnore;    
    private double distanceToFurthest;    
    private double distanceToNearest;    
    private ChildNode smallPivot;    
    public double distSmallPivot;
    
  
    public ChildNode(){
    	this.features = null;
    	this.distanceToParent = Integer.MAX_VALUE;
    	this.coveringRadius = 0;
    	this.accumulatedDistance = 0;
    	this.forwardSimilars = new HashMap<ChildNode, Double>();
    	this.distanceToFurthest = 0;
    	this.similars = new HashSet<ChildNode>();
    	this.toIgnore = false;
    	this.distanceToNearest = Integer.MAX_VALUE; 	
    }
    
    public ChildNode(final EncodedRecord features) {
    	this();
    	this.features= features;
    }
    
    public ChildNode(final EncodedRecord features, double coveringRadius) {
    	this(features);
    	this.coveringRadius = coveringRadius;
    }
    
    public double computeSimilarity(ChildNode other) {    	
    	return JaccardSimilarityCalculator.calculateJaccardSimilarity(other.getFeatures(), this.features);
    }

    public double getAccumulatedDistance() {
    	return this.accumulatedDistance;
    }
    
    public void setAccumulatedDistance(double distance) {
    	this.accumulatedDistance+= distance;
    }
    
    public void resetAccumulatedDistance() {
    	this.accumulatedDistance = 0;
    }
    
    public double getDistanceToFurthest() {
    	return this.distanceToFurthest;
    }
    
    public void setDistanceToFurthest(double distance) {
    	this.distanceToFurthest= distance;
    }
    
    public double getDistanceToNearest() {
    	return this.distanceToNearest;
    }
    
    public void setDistanceToNearest(double distance) {
    	this.distanceToNearest= distance;
    }
    
    public double computeRadius(double threshold){
    	final BitSet bs = this.getFeatures().getBitVector();
    	final int card = bs.cardinality();
    	final int prefixLength = card - ((int) Math.ceil(threshold * card)) + 1;
    	final int pop = 2 * card + prefixLength;
    	final double radius = pop - ((int) Math.floor(2* threshold /(1 + threshold) * pop));
    	return radius;
    }
    
    public EncodedRecord getFeatures() {
    	return this.features;
    }
    
    public int getPopCount() {
    	return this.features.getBitVector().cardinality();
    }

    public double getCoveringRadius() {
        return coveringRadius;
    }
    
    public void setCoveringRadius(final double radius) {
        coveringRadius = radius;
    }

    public double getDistanceToParent() {
        return distanceToParent;
    }
    
    public void setDistanceToParent(final double distanceToParent) {
        this.distanceToParent = distanceToParent;
    }
    
    public RoutingNode getRouting() {
        return routing;
    }
    
    public void setRouting(final RoutingNode routing) {
        this.routing= routing;
    }
    
    public void setSmallPivot(ChildNode sp) {
    	smallPivot= sp;
    }
    
    public ChildNode getSmallPivot() {
    	return smallPivot;
    }
    
    public void deleteSmallPivot() {
    	smallPivot = null;
    }
    
    public String getId() { 
    	return features.getId();
    }
    
    public String getSoundex() {
    	return soundex;
    }
    
    public void setSoundex(String soundex) {
    	this.soundex= soundex;
    }
    
    public int getSourceId() {
    	return sourceId;
    }
    
    public void setSourceId(int sourceId) {
    	this.sourceId= sourceId;
    }
    
    public void addForwardSimilarRecord(ChildNode record, double similarity) {	
    	forwardSimilars.put(record, similarity);
    }
        
    public HashMap<ChildNode, Double> getForwardSimilars() {
    	return forwardSimilars;
    }
    
    public void clearForwardSimilars() {
    	forwardSimilars.clear();
    }
    
    public void addSimilarRecord(ChildNode record) {	
    	similars.add(record);
    }
    
    public void addSimilars(Collection<ChildNode> records) {	
    	similars.addAll(records);
    }
        
    public Set<ChildNode> getSimilars() {
    	return similars;
    }
    
    public void clearSimilars() {
    	similars.clear();
    }
    
    public int getClusterId() {	
    	return clusterId;
    }
    
    public void setClusterId(int clusterId) {
    	this.clusterId= clusterId;
    }
    
    public void resetClusterId() {
    	clusterId= oldClusterId;
    }
    
    public int getOldClusterId() {	
    	return oldClusterId;
    }
    
    public void setOldClusterId(int clusterId){
    	this.oldClusterId = clusterId;
    }
    
    public void setToIgnore(boolean ignore) {
    	toIgnore = ignore;
    }
    
    public boolean getToIgnore() {
    	return toIgnore;
    }
    
    public String toString() {
		return getId();
    }

	@Override
	public int compareTo(ChildNode o) {
		if(getId().equals(o.getId())) {
			return 0;
		}
		else {
			return 1;
		}
	}
	
}