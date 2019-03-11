package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;


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
}