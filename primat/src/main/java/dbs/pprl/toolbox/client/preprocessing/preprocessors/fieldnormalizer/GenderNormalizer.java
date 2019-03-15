package dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class GenderNormalizer extends StringMapper{

	public GenderNormalizer() {
		super(GENDER_REPLACEMENT_MAP);
	}

	public static final Map<String, String> GENDER_REPLACEMENT_MAP = createGenderReplacementMap();

	private static String MALE = "mal";
	private static String FEMALE = "fem";
	private static String OTHER = "oth";
	
    private static Map<String, String> createGenderReplacementMap() {
        final Map<String, String> result = new LinkedHashMap<String, String>();
        result.put("male", MALE);
        result.put("female", FEMALE);
        result.put("m", MALE);
        result.put("f", FEMALE);
        result.put("w", FEMALE);
        result.put("0", MALE);
        result.put("1", FEMALE);
        result.put("other", OTHER);
        result.put("diverse", OTHER);
        result.put("unknown", OTHER);
        result.put("unk", OTHER);
        return Collections.unmodifiableMap(result);
    }	

}
