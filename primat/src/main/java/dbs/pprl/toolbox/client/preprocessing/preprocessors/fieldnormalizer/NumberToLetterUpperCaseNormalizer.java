package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Numbers to upper case letters.
 * @author mfranke
 *
 */
public final class NumberToLetterUpperCaseNormalizer extends CharacterReplacer{

	public NumberToLetterUpperCaseNormalizer() {
		super(OCR_NUMBER_TO_UPPER_CASE_LETTER_REPLACEMENT_MAP);
	}

	public static final Map<Character, String> OCR_NUMBER_TO_UPPER_CASE_LETTER_REPLACEMENT_MAP = createMap();
	
    private static Map<Character, String> createMap() {
        final Map<Character, String> result = new HashMap<Character, String>();
        result.put('0', "O");
        result.put('1', "L");
        result.put('2', "Z");
//        result.put('3', "3");
        result.put('4', "A");
        result.put('5', "S");
        result.put('6', "G");
//        result.put('7', "7");
        result.put('8', "B");
//        result.put('9', "9");
        return Collections.unmodifiableMap(result);
    }	
}