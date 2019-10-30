package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer;


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

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
