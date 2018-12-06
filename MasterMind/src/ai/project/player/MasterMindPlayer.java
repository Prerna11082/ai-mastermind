package ai.project.player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;

import ai.project.common.GuessEvaluator;

public class MasterMindPlayer {
	private int colors;
	private int codeLen;
	private Set<int[]> allGuesses;
	private List<int[]> possibleHints;
	private int[] lastGuess = new int[codeLen];
	private int[] lastHint = new int[2];

	public MasterMindPlayer(int codeLen, int colors) {
		this.codeLen = codeLen;
		this.colors = colors;
		// generate the set of all guesses for given colors & codeLen
		allGuesses = generateAllGuesses();
	}

	/***
	 * Generates all possible guess that can later be eliminated.
	 * @return
	 */
	private Set<int[]> generateAllGuesses() {
		Set<int[]> allGuesses = new HashSet<>();
		int[] guess = new int[codeLen];

		// iterate colors^codeLen times
		for (int i = 0; i < Math.pow(colors, codeLen); ++i) {

			int[] copyOfGuess = new int[guess.length];
			System.arraycopy(guess, 0, copyOfGuess, 0, guess.length);

			// adding the copy of the current guess
			allGuesses.add(copyOfGuess);

			// increment the last element by 1
			++guess[guess.length - 1];

			// iterate over each "column" from right to left
			for (int j = codeLen - 1; j > 0; --j) {
				// if we exceed our numeric base set it back to zero and increment the column to the left
				if (guess[j] == colors) {
					guess[j] = 0;
					++guess[j - 1];
				}
			}
		}
		return allGuesses;
	}

	/**
	 * returns the next guess based on the hint provided.
	 * @param hint
	 * @return
	 */
	public int[] guess(int[] hint) {
		if(null != lastGuess) {
			Set<int[]> filteredGuesses = filterGuessesBasedOnHint(hint);	
			lastGuess = applyMinMax(filteredGuesses); 
		}else {
			for(int i=0; i < codeLen; i++) {
				lastGuess[i] = i > (codeLen/2) ? 0 : 1; 
			}
		}
		return lastGuess;
	}
	/**
	 * Returns the possible guesses that are filtered by provided hint.
	 * @param hint
	 * @return
	 */
	private Set<int[]> filterGuessesBasedOnHint(int[] hint) {
		Set<int[]> filteredGuesses = new HashSet<int[]>(allGuesses);
		Iterator<int[]> iterator = filteredGuesses.iterator();
		while(iterator.hasNext()) {
			int[] possibleGuess = iterator.next();
			if (GuessEvaluator.getColorCorrectPositionIncorrect(possibleGuess, lastGuess)!= lastHint[0] || GuessEvaluator.getColorCorrectPositionCorrect(possibleGuess, lastGuess)!= lastHint[1]) {
				iterator.remove();
			}
		}
		return filteredGuesses;
	}


	/**
	 * The method returns a guess value by applying minmax algorithm in such a way that next time the hint will be able to provide more information (narrows possibilities for next step).
	 * @param filteredGuesses
	 * @return
	 */
	private int[] applyMinMax(Set<int[]> filteredGuesses) {
		int min = Integer.MAX_VALUE;
		int[] guessToNarrowNextPossibilties = new int[codeLen];
		int[] hint = new int[2];
		for (int[] guess : filteredGuesses) {
			int max = 0;
			for (int[] possibleHint : possibleHints) {
				int count = 0;
				for (int[] solution : filteredGuesses) {
					hint[0] = GuessEvaluator.getColorCorrectPositionIncorrect(guess, solution);
					hint[1] = GuessEvaluator.getColorCorrectPositionCorrect(guess, solution);
					if (Arrays.equals(hint, possibleHint))  count++;
				}
				if (count > max) max = count;

			}
			if (max < min) {
				min = max;
				guessToNarrowNextPossibilties = guess;
			}
		}
		return guessToNarrowNextPossibilties;
	}   

}
