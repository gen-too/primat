package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Upper case letters to numbers.
 * 
 * @author mfranke
 *
 */
public final class LetterUpperCaseToNumberNormalizer extends CharacterReplacer{

	public LetterUpperCaseToNumberNormalizer() {
		super(OCR_UPPER_CASE_LETTER_TO_NUMBER_REPLACEMENT_MAP);
	}

	public static final Map<Character, String> OCR_UPPER_CASE_LETTER_TO_NUMBER_REPLACEMENT_MAP = createMap();
	
    private static Map<Character, String> createMap() {
        final Map<Character, String> result = new HashMap<Character, String>();
        result.put('O', "0");
        result.put('L', "1");
        result.put('Z', "2");
//        result.put('3', "3");
        result.put('A', "4");
        result.put('S', "5");
        result.put('G', "6");
//        result.put('7', "7");
        result.put('B', "8");
//        result.put('9', "9");
        return Collections.unmodifiableMap(result);
    }	
}