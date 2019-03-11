package dbs.pprl.toolbox.lu.evaluation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MetricFormat {

	public static final BigDecimal THOUSAND = new BigDecimal(1000);
	public static final int DEFAULT_SCALE_TIME = 2;
	public static final int DEFAULT_SCALE_OTHER = 4;
	public static final RoundingMode ROUNDING_MODE_DEFAULT = RoundingMode.HALF_UP;
	
	private MetricFormat(){
		throw new RuntimeException();
	}
	
	public static BigDecimal doubleToBigDecimal(double value){
		return BigDecimal
			.valueOf(value)
			.setScale(DEFAULT_SCALE_OTHER, ROUNDING_MODE_DEFAULT);
	}
	
	public static Integer doubleToInteger(double value){
		return Double.valueOf(value).intValue();
	}
	
	public static BigDecimal divide(BigDecimal value, BigDecimal devisor){
		return value.divide(devisor, DEFAULT_SCALE_OTHER, ROUNDING_MODE_DEFAULT);
	}
	
	public static BigDecimal convertMsToSeconds(BigDecimal value){
		return value.divide(THOUSAND, DEFAULT_SCALE_TIME, ROUNDING_MODE_DEFAULT);
	}
}