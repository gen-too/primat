package dbs.pprl.toolbox.lu.blocking.metricSpace.pivotSelection;

import java.util.ArrayList;
import java.util.List;

import dbs.pprl.toolbox.lu.blocking.metricSpace.ChildNode;
import dbs.pprl.toolbox.lu.distanceFunctions.DistanceFunction;

public class PivotFullDatasetSelector extends PivotSampleSelector{

	public PivotFullDatasetSelector(int numberOfPivots, DistanceFunction distanceFunction) {
		super(numberOfPivots, Integer.MAX_VALUE, distanceFunction);
	}
	
	@Override
	protected List<ChildNode> getSample(List<ChildNode> fingerprints) {
		return new ArrayList<ChildNode>(fingerprints);
	}
}
