package dbs.pprl.toolbox.lu.evaluation;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.utils.Component;

public abstract class QualityEvaluationComponent extends Component implements QualityEvaluator {

	protected QualityEvaluationComponent(){
		super();
	}
	
	protected QualityEvaluationComponent(boolean parallelExecution){
		super(parallelExecution);
	}
		
	protected final BigDecimal calculateRecall(BigDecimal trueMatches, BigDecimal realMatches){
		if (realMatches.compareTo(BigDecimal.ZERO) <= 0){
			return BigDecimal.ZERO;
		}
		else{
			return trueMatches.divide(realMatches, 4, RoundingMode.HALF_UP);
		}
	}
	
	protected final BigDecimal calculatePrecision(BigDecimal trueMatches, BigDecimal classifiedMatches){
		if (classifiedMatches.compareTo(BigDecimal.ZERO) <= 0){
			return BigDecimal.ZERO;
		}
		else{
			return trueMatches.divide(classifiedMatches, 4, RoundingMode.HALF_UP);
		}
	}
	
	protected final BigDecimal calculateFMeasure(BigDecimal trueMatches, BigDecimal realMatches, 
			BigDecimal classifiedMatches){
		
		final BigDecimal recall = calculateRecall(trueMatches, realMatches);
		final BigDecimal precision = calculatePrecision(trueMatches, classifiedMatches);
		
		return calculateFMeasure(recall, precision);
	}
	
	protected final BigDecimal calculateFMeasure(BigDecimal recall, BigDecimal precision){
		final BigDecimal num = recall.multiply(precision).multiply(new BigDecimal(2));
		final BigDecimal denom = recall.add(precision);
		
		if (denom.compareTo(BigDecimal.ZERO) <= 0){
			return BigDecimal.ZERO;
		}
		else{
			return num.divide(denom, 4, RoundingMode.HALF_UP);
		}
	}
	
	protected abstract long getTrueMatches(Set<? extends CandidatePair> recordPairs);

}