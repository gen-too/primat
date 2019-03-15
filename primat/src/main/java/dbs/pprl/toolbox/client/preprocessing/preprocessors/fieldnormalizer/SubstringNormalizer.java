package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer;


/**
 * Extract a substring.
 * 
 * @author mfranke
 *
 */
public class SubstringNormalizer implements Normalizer{

	private final int start;
	private final int end;
	
	public SubstringNormalizer(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public String normalize(String string) {
		int end = this.end;
		
		if (end >= string.length()){
			end = string.length();
		}
		
		return string.substring(start, end);		
	}
}