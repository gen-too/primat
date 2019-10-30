package dbs.pprl.toolbox.utils;

public class StringUtils {

	public static String upperCamelCaseToTitleCase(String s) {
		   return s.replaceAll(
		      String.format("%s|%s|%s",
		         "(?<=[A-Z])(?=[A-Z][a-z])",
		         "(?<=[^A-Z])(?=[A-Z])",
		         "(?<=[A-Za-z])(?=[^A-Za-z])"
		      ),
		      " "
		   );
		}

	//TODO: Add more case conversions, i.e., sentence case <-> title case <-> all lowercase <-> all uppercase <-> snake case
}