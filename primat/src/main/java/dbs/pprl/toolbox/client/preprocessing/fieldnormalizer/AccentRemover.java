package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;

import java.text.Normalizer;

/**
 * Remove whitespace.
 * 
 * @author mfranke
 *
 */
public class AccentRemover extends RegexReplacer{

	private static final String REGEX = "\\p{InCombiningDiacriticalMarks}+";
	
	public AccentRemover() {
		super(REGEX, "");
	}
	
	@Override
	public String normalize(String string) {
        final StringBuilder decomposed = new StringBuilder(Normalizer.normalize(string, Normalizer.Form.NFD));
        convertRemainingAccentCharacters(decomposed);
		return super.normalize(decomposed.toString());
	}
	
	 private static void convertRemainingAccentCharacters(final StringBuilder decomposed) {
        for (int i = 0; i < decomposed.length(); i++) {
            if (decomposed.charAt(i) == '\u0141') {
                decomposed.deleteCharAt(i);
                decomposed.insert(i, 'L');
            } 
            else if (decomposed.charAt(i) == '\u0142') {
                decomposed.deleteCharAt(i);
                decomposed.insert(i, 'l');
            }
        }
    }
	
}