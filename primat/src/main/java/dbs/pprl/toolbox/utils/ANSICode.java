package dbs.pprl.toolbox.utils;

/**
 * 
 * @author mfranke
 *
 */
public enum ANSICode {

	RESET 	("\u001B[0m"),
	BLACK 	("\u001B[30m"),
	RED 	("\u001B[31m"),
	GREEN 	("\u001B[32m"),
	YELLOW 	("\u001B[33m"),
	BLUE 	("\u001B[34m"),
	PURPLE 	("\u001B[35m"),
	CYAN 	("\u001B[36m"),
	WHITE 	("\u001B[37m"),
	YELLOW_BACKGROUND 	( "\u001B[43m"),
	BLUE_BACKGROUND 	("\u001B[44m"),
	PURPLE_BACKGROUND 	("\u001B[45m"),
	CYAN_BACKGROUND 	("\u001B[46m"),
	WHITE_BACKGROUND 	("\u001B[47m");
	
	private final String code;
	
	ANSICode(String code) {
		this.code = code;
	}
	
	public static String colorize(String string, ANSICode code){
		return code.getCode() + string + ANSICode.RESET.getCode();
	}
	
	private String getCode(){
		return this.code;
	}
}