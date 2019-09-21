package es.laura;

import java.util.Scanner;

/**
 * @author Laura
 * @version 1.0.0
 */
public class Main {

	private Scanner scanner;

	private boolean hunterIsDead = false;

	private boolean hunterIsWinner = false;

	private boolean exit = false;

	private Board board;


	public Main() {
		//Initialize utilities
		scanner = new Scanner(System.in);
		//Initialize data
		initData();
		//Start game
		startGame();
	}

	/**
	 * Init the data of the game.
	 */
	private void initData() {

		//maximum only for the duration that the game could have.
		int width = inputInitData("cells in the board", 4, 10);
		int holes = inputInitData("holes in the board", 0, width * 2);
		int arrows = inputInitData("arrows for the hunter", 0, 100);

		this.board = new Board(width, holes, arrows);

	}

	/**
	 * Gets from user the configurable data
	 *
	 * @param text to show
	 * @param min  value
	 * @param max  value
	 * @return input
	 */
	private int inputInitData(String text, int min, int max) {
		System.out.println("Enter the number of " + text + " (min " + min + ", max " + max + ")");
		int valueToSet;
		if (scanner.hasNextInt()) {
			valueToSet = scanner.nextInt();
			if (valueToSet < min || valueToSet > max) {
				System.out.println("Out of range.");
				scanner.nextLine();
				return inputInitData(text, min, max);
			} else {
				return valueToSet;
			}
		} else {
			System.out.println("Invalid input. Must be a number.");
			scanner.nextLine();
			return inputInitData(text, min, max);
		}
	}

	/**
	 * Start
	 */
	private void startGame() {
		System.out.println("These are the actions you can choose:");
		System.out.println(EnumChoice.getChoices());
		System.out.println("You must press enter after each choice.");
		System.out.println("Game ends when the hunter dies or when he gets the gold.");
		System.out.println("\nWhat do you want to do?\n");
		while (!hunterIsDead && !hunterIsWinner && !exit) {
			EnumChoice choice = EnumChoice.getChoice(scanner.nextLine().toUpperCase());
			if (choice == null) {
				System.out.println("You must enter a valid word.");
			} else {
				switch (choice) {
					case UP:
						break;
					case DOWN:
						break;
					case LEFT:
						break;
					case RIGHT:
						break;
					case ROTATE_LEFT:
						break;
					case ROTATE_RIGHT:
						break;
					case SHOOT:
						break;
					case EXIT:
						if (board.getActualCell() == EnumCell.EXIT) {
							exit = true;
						}
						break;
					case HELP:
						System.out.println(EnumChoice.getChoices());
						System.out.println("-----------------------------------");
						System.out.println(board.getSituation());
						break;
				}
			}
		}

		if (hunterIsDead) {
			System.out.println("Sorry, you are dead :(");
		} else if (hunterIsWinner) {
			System.out.println("Congratulations!!! You are the winner, you have the gold!");
		}
		System.out.println("********************************************************");
		System.out.println("Do you want to play again? (yes/no)");

		String nextLine = scanner.nextLine();
		switch (nextLine) {
			case "yes":
				initData();
				startGame();
				break;
			case "no":
				System.exit(0);
				break;
			default:
				System.out.println("You must enter a valid choice (yes/no)");
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
