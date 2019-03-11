package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;


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

}