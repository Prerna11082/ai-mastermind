package ai.project.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GuessEvaluator {
	
	private static final int EMPTY = 0;
	private int[] secretCode;
	private int colors;
	
	private Map<Integer,Integer> colorMap = new HashMap<Integer,Integer>();
	
	/** TODO Think about what will be stores as status - whether there was a win/lose instead of just a plain boolean flag.
	 * Do we need to constrain the number of steps/guess allowed?
	 */
	private boolean gameOver;
	
	/**
	 * Constructor: code can be set only once i.e. during instantiation.
	 * @param code
	 */
	public GuessEvaluator(int[] code, int colors) {
		secretCode = code;
		this.colors = colors;
		for(int c: code) {
			colorMap.put(c, null != colorMap.get(c) ? colorMap.get(c) + 1: 1);
		}
	}
		
	/**
	 * TODO check black and whites. 
	 * Compares the guess with the secret and generates a sequence of hints and updates the status (gameOver) of the game.
	 * @param guess
	 * @return
	 */
	public int[] evaluate(int[] guess) {
		if(guess.length != secretCode.length) return null;
		int[] h = new int[2];
		Arrays.fill(h, EMPTY);		
		// TODO logic for evaluation.
		
		return h;
	}
	
	/**
	 * Returns the game status.
	 * @return
	 */
	public boolean isGameOver() {
		return gameOver;
	}
	
}
