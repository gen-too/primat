package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;

public final class StandardNumberNormalizer implements Normalizer{

	private final NormalizerChain normalizer;
	
	public StandardNumberNormalizer() {
		this.normalizer = new NormalizerChain();
		this.normalizer.add(new LetterLowerCaseToNumberNormalizer());
		this.normalizer.add(new LetterUpperCaseToNumberNormalizer());
		this.normalizer.add(new NonDigitRemover());
	}
	
	@Override
	public String normalize(String string) {
		return this.normalizer.normalize(string);
	}
}