package ai.project.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GuessEvaluator {
	
	private static final int EMPTY = 0;
	private int[] secretCode;
	
	
	/** TODO Think about what will be stores as status - whether there was a win/lose instead of just a plain boolean flag.
	 * Do we need to constrain the number of steps/guess allowed?
	 */
	private boolean foundAnswer;
	
	/**
	 * Constructor: code can be set only once i.e. during instantiation.
	 * @param code
	 */
	public GuessEvaluator(int[] code) {
		secretCode = code;
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
		h[1] = getColorCorrectPositionCorrect(guess, secretCode);
		h[0] = getColorCorrectPositionIncorrect(guess, secretCode);
		if (h[1] == secretCode.length) {
			foundAnswer = true;
		}
		return h;
	}
	
	/***
	 * check for number of correct colors but in incorrect position from the guess
	 * @param guess
	 * @param secretCode
	 * @return
	 */
    public static int getColorCorrectPositionIncorrect(int[] guess, int[] secretCode) {
    	int count = 0;
    	Map<Integer,Integer> secColorCount = new HashMap<Integer,Integer>();
    	/** Create a map of color counts in the secret **/
    	for(int secret: secretCode) {
    		secColorCount.put(secret, secColorCount.getOrDefault(secret,0)+1);
    	}
    	
    	/** Check against the map, the number of misplaced colors in guess **/
    	for(int i = 0; i< guess.length; i++) {
    		int curr = guess[i];
    		if(curr != secretCode[i] && null != secColorCount.get(curr) && secColorCount.get(curr)!=0) {
    			count++;
    		}
    		if(null != secColorCount.get(curr)) {
    			secColorCount.put(curr,secColorCount.get(curr)-1);
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
    	/** iterate over the arrays **/
        for (int i = 0; i < guess.length; ++i) {
        	/** if they have the same value at the same point in the array increment the val counter **/
            if (guess[i] == secretCode[i]) {
                ++count;
            }
        }
        return count;
    }
	
	/**
	 * return true if an answer was already guessed correctly.
	 * @return
	 */
	public boolean isFoundAnswer() {
		return foundAnswer;
	}
	
}