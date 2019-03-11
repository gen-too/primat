package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;


public final class StandardStringNormalizer implements Normalizer {

	private final NormalizerChain normalizer;
	
	public StandardStringNormalizer() {
		this.normalizer = new NormalizerChain();
		this.normalizer.add(new WhitespaceRemover());
		this.normalizer.add(new UmlautNormalizer());
		this.normalizer.add(new AccentRemover());
		this.normalizer.add(new PunctuationRemover());
		this.normalizer.add(new NumberToLetterLowerCaseNormalizer());
		this.normalizer.add(new LowerCaseNormalizer());
	}
	
	@Override
	public String normalize(String string) {
		return this.normalizer.normalize(string);
	}
}