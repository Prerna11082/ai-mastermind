package ai.project.game;

public enum Hints{
	EMPTY(-1),
	MATCH(1),
	COLORMATCH(0);
	
	Hints(int code){
		this.code = code;
	}
	
	private int code;
		
	public int getCode() {
		return code;
	}	
	
}
