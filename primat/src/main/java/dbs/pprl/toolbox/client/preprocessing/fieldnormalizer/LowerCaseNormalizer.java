package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;


/**
 * To lower case string.
 * @author mfranke
 *
 */
public class LowerCaseNormalizer implements Normalizer{

	@Override
	public String normalize(String string) {
		return string.toLowerCase();
	}

}
