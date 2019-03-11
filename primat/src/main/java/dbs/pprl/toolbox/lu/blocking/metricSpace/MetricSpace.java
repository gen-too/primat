package dbs.pprl.toolbox.lu.blocking.metricSpace;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dbs.pprl.toolbox.lu.blocking.BlockingComponent;
import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.blocking.metricSpace.indexing.IndexMethod;
import dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection.StaticPivotSelector;
import dbs.pprl.toolbox.lu.distanceFunctions.DistanceFunction;
import dbs.pprl.toolbox.lu.evaluation.MetricFormat;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;

public class MetricSpace extends BlockingComponent{

	private StaticPivotSelector staticPivotSelector;
	private IndexMethod indexer;
	private double threshold;
	private DistanceFunction distanceFunction;
	
	public MetricSpace(StaticPivotSelector staticPivotSelector, IndexMethod indexer, double threshold, DistanceFunction distanceFunction){
		this.staticPivotSelector = staticPivotSelector;
		this.indexer = indexer;
		this.threshold = threshold;
		this.distanceFunction = distanceFunction;
	}
	
	@Override
	protected Set<CandidatePair> getCandidatePairsConcrete(List<EncodedRecord> recordsPartyA,
			List<EncodedRecord> recordsPartyB) {
		final long numberOfRecordsPartyA = recordsPartyA.size();
		final long numberOfRecordsPartyB = recordsPartyB.size();
		final long sizeCartesianProduct = numberOfRecordsPartyA * numberOfRecordsPartyB;
		
		final Set<CandidatePair> candidates = this.getCandidatesMs(recordsPartyA, recordsPartyB);
		
		final long numberOfCandidates = candidates.size();		
		this.collectReductionRatio(numberOfCandidates, sizeCartesianProduct);
		
		return candidates;
	}
	
	private Set<CandidatePair> getCandidatesMs(List<EncodedRecord> recordsPartyA,
			List<EncodedRecord> recordsPartyB){
		
		// Indexing
		final List<RoutingNode> pivots = this.indexRecords(recordsPartyB);
		System.out.println("Indexing done.");
		final List<ChildNode> queries = this.prepareQueryRecords(recordsPartyA);
		
		
		// Matching
		System.out.println("Start matching.");
		final Set<CandidatePair> candidates = new HashSet<CandidatePair>();
		
		for (RoutingNode pivot : pivots){
			final double pivotRadius = pivot.getCoveringRadius();
			
			for (ChildNode query : queries){
				final double queryRadius = query.getCoveringRadius();
				final double distanceQueryPivot = this.distanceFunction.computeDistance(query, pivot);
				
				if (distanceQueryPivot <= pivotRadius + queryRadius){
					for (ChildNode entry : pivot.getEntries()){
						final double x = Math.abs(distanceQueryPivot - entry.getDistanceToParent());
						if (x <= queryRadius){
							final double distance = this.distanceFunction.computeDistance(query, entry);
							if (distance <= queryRadius){
								final EncodedRecord recA = entry.getFeatures();
								final EncodedRecord recB = query.getFeatures();
								final CandidatePair pair = new CandidatePair(recA, recB);
								candidates.add(pair);
//								System.out.println(pair);
							}
						}
					}
				}
			}
		}
		System.out.println("cands: " + candidates.size());
		return candidates;
	}
	
	private List<ChildNode> prepareQueryRecords(List<EncodedRecord> records) {
		final List<ChildNode> queries = new ArrayList<ChildNode>(records.size());
		for (final EncodedRecord rec : records){
			final ChildNode cn = new ChildNode(rec);
			final double radius = cn.computeRadius(this.threshold);
			cn.setCoveringRadius(radius);
			queries.add(cn);
		}
		return queries;
	}
	
	private List<RoutingNode> indexRecords(List<EncodedRecord> records){
		final List<ChildNode> recs = new ArrayList<>(records.size());
		
		for (final EncodedRecord rec : records){
			recs.add(new ChildNode(rec));
		}
		
		final List<RoutingNode> pivots = this.staticPivotSelector.findPivots(recs);
		System.out.println("Find pivots done. " + pivots.size());
		this.indexer.indexRecords(recs, pivots);
		
		return pivots;
	}

	private void collectReductionRatio(long numberOfCandidates, long sizeCartesianProduct){
		final BigDecimal candidates = BigDecimal.valueOf(numberOfCandidates);
		final BigDecimal cartesianProduct = BigDecimal.valueOf(sizeCartesianProduct);
		final BigDecimal proportion = MetricFormat.divide(candidates, cartesianProduct);
		final BigDecimal reductionRatio = BigDecimal.ONE.subtract(proportion);
		this.metrics.put(REDUCTION_RATIO, reductionRatio);
	}
}
