package dbs.pprl.toolbox.client.common;

public class StringConverter {

	public static Object convert(String s, Class<?> t){
		if (t.equals(int.class)){
			return Integer.valueOf(s).intValue();
		}
		else if (t.equals(Integer.class)){
			return Integer.valueOf(s);
		}
		else if (t.equals(double.class)){
			return Double.valueOf(s).doubleValue();
		}
		else if (t.equals(Double.class)){
			return Double.valueOf(s);
		}
		else if (t.equals(Long.class)){
			return Long.valueOf(s);
		}
		else if (t.equals(long.class)){
			return Long.valueOf(s).longValue();
		}
		else if (t.equals(boolean.class)){
			return Boolean.parseBoolean(s);
		}
		else if (t.equals(Boolean.class)){
			return Boolean.parseBoolean(s);
		}
		else if (t.equals(float.class)){
			return Float.parseFloat(s);
		}
		else if (t.equals(Float.class)){
			return Float.parseFloat(s);
		}
		else if (t.equals(String.class)){
			return s;
		}
		else if (t.equals(char.class)){
			return s.charAt(0);
		}
		else if (t.equals(Character.class)){
			return s.charAt(0);
		}
		else{
			return null;
		}
	}
}