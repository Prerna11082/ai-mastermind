package ai.project.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GuessEvaluator {
	
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
	 * TODO To test and update accordingly. 
	 * Compares the guess with the secret and generates a sequence of hints and updates the status (gameOver) of the game.
	 * @param guess
	 * @return
	 */
	public Hints[] evaluate(int[] guess) {
		if(guess.length != secretCode.length) return null;
		Hints[] h = new Hints[secretCode.length];
		Arrays.fill(h, Hints.EMPTY);
		boolean[] visited = new boolean[colors];
		
		int j = 0;
		for(int i = 0; i < guess.length; i++) {
			if(secretCode[i] == guess[i]) {
				h[j] = Hints.MATCH;
				j++;
			}
			/** assuming no duplicates allowed. **/
			else if(!visited[guess[i]] && null != colorMap.get(guess[i])) { /** TODO verify expectation of game hints **/
				visited[guess[i]] = true;
				h[j] = Hints.COLORMATCH;
			}
		}
		
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
