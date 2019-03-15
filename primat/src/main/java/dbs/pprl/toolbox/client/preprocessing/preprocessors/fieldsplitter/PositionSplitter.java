package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldsplitter;


public class PositionSplitter implements Splitter{
	
	private final int position;
	private final static int PARTS = 2;

	public PositionSplitter(int position) {
		this.position = position;
	}

	@Override
	public String[] split(String string) {
		final String[] result = new String[2];
		if (position < string.length()){
			result[0] = string.substring(0, this.position);
			result[1] = string.substring(this.position);
		}
		else{
			result[0] = string;
			result[1] = "";
		}
		return result;
	}
	
	@Override
	public int parts() {
		return PARTS;
	}
}