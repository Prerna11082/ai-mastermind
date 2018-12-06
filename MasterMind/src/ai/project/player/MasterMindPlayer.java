package ai.project.player;

import java.util.HashSet;
import java.util.Set;

public class MasterMindPlayer {
	private int colors;
	private int codeLen;
	private Set<int[]> allGuesses;

	public MasterMindPlayer(int codeLen, int colors) {
		this.codeLen = codeLen;
		this.colors = colors;
		// generate the set of all possible combinations of colours
        allGuesses = generateAllGuesses(codeLen, colors);
	}

	
	private static Set<int[]> generateAllGuesses(int holes, int colours) {
        Set<int[]> allPossibilities = new HashSet<>();
        int[] possibility = new int[holes];

        // want to iterate colours to the power of holes times
        for (int i = 0; i < Math.pow(colours, holes); ++i) {

            // create a copy of the possibility so that we aren't adding references to the same object multiple times
            int[] copyOfPoss = new int[possibility.length];
            System.arraycopy(possibility, 0, copyOfPoss, 0, possibility.length);

            // add the copy of the current possibility
            allPossibilities.add(copyOfPoss);

            // increment the last element by 1
            ++possibility[possibility.length - 1];

            // iterate over each "column" from right to left
            for (int j = holes - 1; j > 0; --j) {
                // if we exceed our numeric base set it back to zero and increment the column to the left
                if (possibility[j] == colours) {
                    possibility[j] = 0;
                    ++possibility[j - 1];
                }
            }
        }
        return allPossibilities;
    }
	
	public int[] guess(int[] hints) {
		
		
		return null;
	}
	
		
}
