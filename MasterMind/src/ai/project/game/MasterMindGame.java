package ai.project.game;

import java.util.Arrays;
import java.util.Scanner;

import ai.project.common.GuessEvaluator;
import ai.project.player.MasterMindPlayer;

public class MasterMindGame {

	private static final String IGNORE_SYMBOLS = "(\r\n|[\n\r\u2028\u2029\u0085])?";
	private static final int ALLOWED_STEPS = 10;

	public static void main(String[] args) {
		/** TODO Check if we need console read/ file read for inputs. **/
//		System.out.println("Enter number of colors and the length:");
//		Scanner s = new Scanner(System.in);
		int colors = 9;//Integer.parseInt(s.nextLine());
//		s.skip(IGNORE_SYMBOLS);
		int codeLen = 6;//Integer.parseInt(s.nextLine());
//		s.close();
		System.out.println("Index,Win,Strategy,Time,Steps,Secret");
		for (int i=0; i<1000; i++) {
			int[] secret = SecretCodeGenerator.generate(codeLen, colors);
			start(colors, codeLen, i, 0, secret);
			start(colors, codeLen, i, 1, secret);
		}
	}

	private static void start(int colors, int codeLen, int instance, int strategy, int [] secret) {
//		int[] secret = SecretCodeGenerator.generate(codeLen, colors);
//		int [] secret = new int [] {7,8,8,5,3,0};//{8,3,2,1,5,0};//
		GuessEvaluator evaluator = new GuessEvaluator(secret);
		MasterMindPlayer player = new MasterMindPlayer(codeLen, colors, strategy);

		int[] hints = new int[2];

		/** Setting default empty hints **/
		Arrays.fill(hints, 0);
		int stepCount = 0;
		while (!evaluator.isFoundAnswer() && stepCount < ALLOWED_STEPS) {
			stepCount++;
			int[] guess = player.guess(hints);
//			System.out.println("Step " + stepCount + ": " + Arrays.toString(guess));
			hints = evaluator.evaluate(guess);
//			System.out.println("Hint " + stepCount + ":" + Arrays.toString(hints));
		}

		if (!evaluator.isFoundAnswer()) {
			System.out.println(instance + "," + "0," + strategy + "," + player.getTotalTime() + ","+ stepCount + ","+ Arrays.toString(secret).replaceAll(","," "));
//			System.out.println("Oops, the secret is: " + Arrays.toString(secret));
//			System.out.println("Total time taken: " + player.getTotalTime());
		} else {
			System.out.println(instance + "," + "1," + strategy + "," + player.getTotalTime() + ","+ stepCount + ","+ Arrays.toString(secret).replaceAll(","," "));
//			System.out.println("You win!! With just " + stepCount + " guesses");
//			System.out.println("You are right, the secret was: " + Arrays.toString(secret));
//			System.out.println("Total time taken: " + player.getTotalTime());
		}

	}
}
