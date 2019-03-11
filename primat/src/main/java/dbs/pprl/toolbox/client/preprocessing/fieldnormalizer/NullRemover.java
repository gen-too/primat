package dbs.pprl.toolbox.client.preprocessing.fieldnormalizer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class NullRemover extends StringMapper{

	public NullRemover() {
		super(NULL_REPLACEMENT_MAP);
	}

	public static final Map<String, String> NULL_REPLACEMENT_MAP = createNullReplacementMap();

	
    private static Map<String, String> createNullReplacementMap() {
        final Map<String, String> result = new LinkedHashMap<String, String>();
        result.put("NULL", "");
        result.put("null", "");
        return Collections.unmodifiableMap(result);
    }	

}
