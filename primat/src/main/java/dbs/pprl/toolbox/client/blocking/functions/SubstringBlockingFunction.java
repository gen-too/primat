package dbs.pprl.toolbox.client.blocking.functions;


public class SubstringBlockingFunction implements BlockingFunction{

	private final int startIndex;
	private final int endIndex;

	public SubstringBlockingFunction(int startIndex, int endIndex) {
		if (startIndex >= endIndex){
			throw new IllegalArgumentException("startIndex is greater than endIndex!");
		}
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	private static boolean isEmptyString(String string){
		return string == null || string.length() == 0;
	}
		
	@Override
	public String apply(String attribute) {			
		if (isEmptyString(attribute)){
			return "";
		}
		else{
			final int length = attribute.length();
			final int customEndIndex = length < endIndex ? length : endIndex;
			return attribute.substring(startIndex, customEndIndex);
		}
	}	
}