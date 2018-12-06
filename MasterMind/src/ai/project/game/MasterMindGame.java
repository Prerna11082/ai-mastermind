package ai.project.game;

import java.util.Arrays;
import java.util.Scanner;

import ai.project.common.GuessEvaluator;
import ai.project.player.MasterMindPlayer;

public class MasterMindGame{

	private static final String IGNORE_SYMBOLS = "(\r\n|[\n\r\u2028\u2029\u0085])?";

	public static void main(String[] args) {
		/** TODO Check if we need console read/ file read for inputs.**/
		Scanner s = new Scanner(System.in);
		int colors = Integer.parseInt(s.nextLine());
		s.skip(IGNORE_SYMBOLS);
		int codeLen = Integer.parseInt(s.nextLine());
	    s.close();
        start(colors,codeLen);
	}

	private static void start(int colors,int codeLen) {
		GuessEvaluator evaluator = new GuessEvaluator(SecretCodeGenerator.generate(codeLen,colors), colors);
		MasterMindPlayer player = new MasterMindPlayer(codeLen,colors);
		
		int[] hints = new int[2];
		
		/** Setting default empty hints **/
		Arrays.fill(hints, 0); 
		
		while(!evaluator.isGameOver()) {
			hints = evaluator.evaluate(player.guess(hints));			
		}
		
		/** TODO based on win/lose update message to the user - might need to check with evaluator to provide this information.**/
	}	
}
