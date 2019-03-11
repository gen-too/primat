package dbs.pprl.toolbox.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import dbs.pprl.toolbox.lu.evaluation.MetricCollector;
import dbs.pprl.toolbox.lu.evaluation.MetricFormat;

public abstract class Component implements MetricCollector{

	public static final boolean DEFAULT_PARALLEL_EXECUTION = true;
	
	protected Map<String, Number> metrics;
	protected boolean parallelExecution;
	protected long startTime;
	protected long endTime;
	
	protected Component(){
		this(DEFAULT_PARALLEL_EXECUTION);
	}
	
	protected Component(boolean parallelExecution){
		this.parallelExecution = parallelExecution;
		this.metrics = new HashMap<String, Number>();
	}
		
	public static BigDecimal determineRuntime(long startTime, long endTime){
		final BigDecimal msTimePostprocessing = BigDecimal.valueOf(endTime - startTime);
		return MetricFormat.convertMsToSeconds(msTimePostprocessing);
	}
	
	public BigDecimal determineRuntime(){
		return determineRuntime(this.startTime, this.endTime);
	}
	
	@Override
	public Map<String, Number> getMetrics() {
		return this.metrics;
	}
}
