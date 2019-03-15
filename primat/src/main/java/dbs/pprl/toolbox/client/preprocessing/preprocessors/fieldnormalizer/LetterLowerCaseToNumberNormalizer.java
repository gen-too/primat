package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Lower case letters to numbers.
 * @author mfranke
 *
 */
public final class LetterLowerCaseToNumberNormalizer extends CharacterReplacer{

	public LetterLowerCaseToNumberNormalizer() {
		super(OCR_LOWER_CASE_LETTER_TO_NUMBER_REPLACEMENT_MAP);
	}

	public static final Map<Character, String> OCR_LOWER_CASE_LETTER_TO_NUMBER_REPLACEMENT_MAP = createMap();
	
    private static Map<Character, String> createMap() {
        final Map<Character, String> result = new HashMap<Character, String>();
        result.put('o', "0");
        result.put('l', "1");
        result.put('z', "2");
//        result.put('3', "3");
        result.put('q', "4");
        result.put('s', "5");
//        result.put('6', "6");
//        result.put('7', "7");
//        result.put('8', "8");
        result.put('g', "9");
        return Collections.unmodifiableMap(result);
    }	
}