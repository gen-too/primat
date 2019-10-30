package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer;


/**
 * Remove any trailing and leading whitespace.
 * @author mfranke
 *
 */
public class TrimNormalizer implements Normalizer{

	@Override
	public String normalize(String string) {
		return string.trim();
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}