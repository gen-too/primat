package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer;


/**
 * To upper case.
 * 
 * @author mfranke
 *
 */
public class UpperCaseNormalizer implements Normalizer{

	@Override
	public String normalize(String string) {
		return string.toUpperCase();
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}