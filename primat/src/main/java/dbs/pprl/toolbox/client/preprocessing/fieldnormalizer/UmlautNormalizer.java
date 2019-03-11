package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Replace umlauts by equivalents.
 * @author mfranke
 *
 */
public final class UmlautNormalizer extends CharacterReplacer{

	public UmlautNormalizer() {
		super(UMLAUT_REPLACEMENT_MAP);
	}

	public static final Map<Character, String> UMLAUT_REPLACEMENT_MAP = createUmlautReplacementMap();

    private static Map<Character, String> createUmlautReplacementMap() {
        final Map<Character, String> result = new HashMap<Character, String>();
        result.put('\u00e4', "ae");
        result.put('\u00c4', "AE");
        result.put('\u00f6', "oe");
        result.put('\u00d6', "OE");
        result.put('\u00fc', "ue");
        result.put('\u00dc', "UE");
        result.put('\u00df', "ss");
        return Collections.unmodifiableMap(result);
    }	
}