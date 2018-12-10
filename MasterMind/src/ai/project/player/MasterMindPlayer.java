package ai.project.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ai.project.common.GuessEvaluator;

/**
 * @author shamalip, adityan, prernas 
 * The AI player implementation.
 */
public class MasterMindPlayer {
	private int colors;
	private int codeLen;
	private Set<int[]> allGuesses;
	private List<int[]> possibleHints;
	private Map<int[], Integer> hintMatrix;
	private int[] lastGuess = null;
	private long generateGuessTime = 0;
	private long filterTime = 0;
	private long guessingTime = 0;
	private long totalTime;
	private int strategy = 0;

	public long getTotalTime() {
		return generateGuessTime + filterTime + guessingTime + totalTime;
	}

	public MasterMindPlayer(int codeLen, int colors, int str) {
		this.codeLen = codeLen;
		this.colors = colors;
		strategy = str;
		/** generate the set of all guesses for given colors & codeLen **/
		generateAllGuesses();
		generateAllPossibleHints();
	}

	/**
	 * Generate an list of all possible hints so as to refer them in min-max
	 * algorithm.
	 *
	 **/
	private void generateAllPossibleHints() {
		possibleHints = new ArrayList<>();
		hintMatrix = new HashMap<int[], Integer>();
		for (int i = 0; i <= codeLen; i++) {
			for (int j = 0; j <= codeLen; j++) {
				if (!(j == codeLen - 1 && i == 1) && i + j <= codeLen) {
					int[] hintItem = new int[2];
					hintItem[0] = i;
					hintItem[1] = j;
					possibleHints.add(hintItem);
					hintMatrix.put(hintItem, 0);
				}
			}
		}
	}

	/***
	 * Generates all possible guess that can later be eliminated.
	 *
	 **/
	private void generateAllGuesses() {
		long startTime = System.currentTimeMillis();
		allGuesses = new HashSet<>();
		int[] guess = new int[codeLen];

		/** iterate colors^codeLen times **/
		for (int i = 0; i < Math.pow(colors, codeLen); ++i) {

			int[] copyOfGuess = new int[guess.length];
			System.arraycopy(guess, 0, copyOfGuess, 0, guess.length);

			/** adding the copy of the current guess **/
			allGuesses.add(copyOfGuess);

			/** increment the last element by 1 **/
			++guess[guess.length - 1];

			/** iterate over each "column" from right to left **/
			for (int j = codeLen - 1; j > 0; --j) {
				/***
				 * if we exceed our numeric base set it back to zero and increment the column to
				 * the left
				 **/
				if (guess[j] == colors) {
					guess[j] = 0;
					++guess[j - 1];
				}
			}
		}
//		System.out.println("****Number of total Possibilities******   "+allGuesses.size());
		long endTime = System.currentTimeMillis();
		generateGuessTime = endTime - startTime;
//		System.out.println("Generated All Guesses in: " + generateGuessTime + " ms");
	}

	/**
	 * returns the next guess based on the hint provided.
	 * 
	 * @param hint
	 * @return
	 */
	public int[] guess(int[] hint) {
		if (null != lastGuess) {
			filterGuessesBasedOnHint(hint);
			if (strategy == 0) {
				lastGuess = applyMinMax();
			} else {
				lastGuess = getRandomGuess();
			}
		} else {
			lastGuess = new int[codeLen];
			for (int i = 0; i < codeLen; i++) {
				lastGuess[i] = i >= (codeLen / 2) ? 0 : 1;
			}
		}
		return lastGuess;
	}

	/**
	 * Returns the possible guesses that are filtered by provided hint.
	 * 
	 * @param hint
	 * @return
	 */
	private void filterGuessesBasedOnHint(int[] hint) {
		long startTime = System.currentTimeMillis();
		long initSize = allGuesses.size();
		Iterator<int[]> iterator = allGuesses.iterator();
		while (iterator.hasNext()) {
			int[] possibleGuess = iterator.next();
			if (GuessEvaluator.getColorCorrectPositionIncorrect(possibleGuess, lastGuess) != hint[0]
					|| GuessEvaluator.getColorCorrectPositionCorrect(possibleGuess, lastGuess) != hint[1]) {
				iterator.remove();
			}
		}
		long endTime = System.currentTimeMillis();
		long finalSize = allGuesses.size();
		filterTime += endTime - startTime;
//		System.out.println(
//				"Reduced guesses from " + initSize + " to " + finalSize + " in: " + (endTime - startTime) + "ms");
	}
	
	private int[] getRandomGuess() {
        Iterator<int[]> iterator = allGuesses.iterator();
        int[] nextGuess = new int[codeLen];

        if (iterator.hasNext()) {
            nextGuess = iterator.next();
        }
        return nextGuess;
    }

	/**
	 * The method returns a guess value by applying minimax algorithm in such a way
	 * that next time the hint will be able to provide more information (narrows
	 * possibilities for next step).
	 * 
	 * @param allGuesses
	 * @return
	 */
	private int[] applyMinMax() {
		long startTime = System.currentTimeMillis();
		int min = Integer.MAX_VALUE;
		int cutOff = Integer.MAX_VALUE;
		int[] minimizedGuess = new int[codeLen];
//		System.out.println("****Number of Possibilities******   "+allGuesses.size());
		int[] hint = new int[2];
		for (int[] guess : allGuesses) {
			int max = 0;
			for (int[] possibleHint : possibleHints) {
 				int count = 0;
 				for (int[] solution : allGuesses) {
 					hint[0] = GuessEvaluator.getColorCorrectPositionIncorrect(guess, solution);
 					hint[1] = GuessEvaluator.getColorCorrectPositionCorrect(guess, solution);
 					if (Arrays.equals(hint, possibleHint))  count++;
 				}
 				if (count > max) max = count;
			}
			// NEW
//			for (int[] solution : allGuesses) {
//				hint = GuessEvaluator.getHint(guess, solution);
//				hintMatrix.put(hint, hintMatrix.getOrDefault(hint, 0) + 1);
//			}
//			max = maxHintOccurence(hintMatrix);
//			System.out.println("Count hello:" + max);
			if (max < min) {
				min = max;
				minimizedGuess = guess;
				if(min/allGuesses.size() < 0.20) {
					break;
				}
			}
		}
		long endTime = System.currentTimeMillis();
		guessingTime += endTime - startTime;
//		System.out.println("Made a guess in " + (endTime - startTime) + "ms\n");
		return minimizedGuess;
	}

	public <K, V extends Comparable<V>> V maxHintOccurence(Map<K, V> map) {
		Entry<K, V> maxEntry = Collections.max(map.entrySet(),
				(Entry<K, V> e1, Entry<K, V> e2) -> e1.getValue().compareTo(e2.getValue()));
		return maxEntry.getValue();
	}
}
