package dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer;


/**
 * Extract a substring.
 * 
 * @author mfranke
 *
 */
public class SubstringNormalizer implements Normalizer{

	private int start;
	private int end;
	
	
	public SubstringNormalizer(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public void setEnd(int end) {
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
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.start + "," + this.end + ")";
	}
}