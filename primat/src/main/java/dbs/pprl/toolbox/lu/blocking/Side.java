package dbs.pprl.toolbox.lu.blocking;

public enum Side {
	LEFT,
	RIGHT;
	
	public static Side other(Side side){
		switch (side){
			case LEFT: return RIGHT;
			
			case RIGHT: return LEFT;
			
			default: return null;
		}
	}
}