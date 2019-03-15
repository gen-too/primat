package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Numbers to lower case letters.
 * 
 * @author mfranke
 *
 */
public final class NumberToLetterLowerCaseNormalizer extends CharacterReplacer{

	public NumberToLetterLowerCaseNormalizer() {
		super(OCR_NUMBER_TO_LETTER_LOWER_CASE_REPLACEMENT_MAP);
	}

	public static final Map<Character, String> OCR_NUMBER_TO_LETTER_LOWER_CASE_REPLACEMENT_MAP = createMap();
	
    private static Map<Character, String> createMap() {
        final Map<Character, String> result = new HashMap<Character, String>();
        result.put('0', "o");
        result.put('1', "l");
        result.put('2', "z");
//        result.put('3', "3");
        result.put('4', "q");
        result.put('5', "s");
//        result.put('6', "6");
//        result.put('7', "7");
//        result.put('8', "8");
        result.put('9', "g");
        return Collections.unmodifiableMap(result);
    }	
}