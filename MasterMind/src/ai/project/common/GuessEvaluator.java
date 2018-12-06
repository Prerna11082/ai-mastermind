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
		h[0] = getColorCorrectPositionCorrect(guess, secretCode);
		h[1] = getColorCorrectPositionIncorrect(guess, secretCode);
		if (h[0] == secretCode.length) {
			gameOver = true;
		}
		return h;
	}
	
	/***
	 * check for number of correct colors but in incorrect position form the guess
	 * @param guess
	 * @param secretCode
	 * @return
	 */
    public static int getColorCorrectPositionIncorrect(int[] guess, int[] secretCode) {
        int count = 0;
        int[] guessA = new int[guess.length];
        int[] secretA = new int[secretCode.length];
        
        // create a copy of the two input arrays
        System.arraycopy(guess, 0, guessA, 0, guess.length);
        System.arraycopy(secretCode, 0, secretA, 0, secretCode.length);
        
        // check each of the items in corresponding positions
        for (int i = 0; i < guess.length; ++i) {
        	// if they are the same, i.e. in the correct position
            if (guessA[i] == secretA[i]) {
            	// mark as dealt with (since blacks() handles that) by subtracting the current iteration and an arbitrary sufficiently different value from each
                guessA[i] = 0 - i - 50;
                secretA[i] = 0 - i - 100;
            }
        }
        
        // compare each item in oneA to every item in twoA
        for (int i = 0; i < guess.length; ++i) {
            for (int j = 0; j < guess.length; ++j) {
            	// don't want to check items at the same index - we did that earlier - only ones in different positions
                if (i != j && guessA[i] == secretA[j]) {
                	// increment the counter of whites
                    ++count;
                    // mark each one as dealt with
                    guessA[i] = 0 - i - 50;
                    secretA[j] = 0 - j - 100;
                    break;
                }
            }
        }
        return count;
    }
    
    /***
     * check for number of correct colors in correct positions from the guess
     * @param guess
     * @param secretCode
     * @return
     */
    public static int getColorCorrectPositionCorrect(int[] guess, int[] secretCode) {
    	int count = 0;
    	// iterate over the arrays
        for (int i = 0; i < guess.length; ++i) {
        	// if they have the same value at the same point in the array increment the val counter
            if (guess[i] == secretCode[i]) {
                ++count;
            }
        }
        return count;
    }
	
	/**
	 * Returns the game status.
	 * @return
	 */
	public boolean isGameOver() {
		return gameOver;
	}
	
}
