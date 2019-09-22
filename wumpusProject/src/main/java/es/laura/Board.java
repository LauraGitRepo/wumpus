package es.laura;

import java.util.*;

/**
 * @author Laura
 * @version 1.0.0
 */
public class Board {

	/**
	 * Width of the board (width * width)
	 */
	private int width;

	/**
	 * Number of holes in the board (Max 2*width)
	 */
	private int holes;

	/**
	 * Number of arrows of the hunter
	 */
	private int arrows;

	/**
	 * Board with all the box data
	 */
	private EnumBox[][] gameBoard;

	/**
	 * Board info recovered for the hunter
	 */
	private EnumBox[][] hunterBoard;

	/**
	 * Actual box of the game: message, row, column and orintation
	 */
	private String lastMessage = EnumBox.EXIT.getMessage();

	private int actualRow;

	private int actualCol;

	private EnumOrientation actualOrientation = EnumOrientation.NORTH;

	/**
	 * Util for generate random numbers
	 */
	private Random random;

	/**
	 * Construuctor
	 *
	 * @param width  of the board
	 * @param holes  in the board
	 * @param arrows of the hunter
	 */
	Board(int width, int holes, int arrows) {
		this.width = width;
		this.holes = holes;
		this.arrows = arrows;
		this.actualCol = 0;
		this.actualRow = width - 1;
		random = new Random();
		createBoard();

		for (EnumBox[] enumBoxes : gameBoard) System.out.println(Arrays.toString(enumBoxes));

		setAndPrintMessage();
	}

	/**
	 * Create a random but possible board
	 */
	private void createBoard() {
		gameBoard = new EnumBox[width][width];
		hunterBoard = new EnumBox[width][width];

		//Init with empty boxs
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				gameBoard[i][j] = EnumBox.EMPTY;
				hunterBoard[i][j] = EnumBox.UNKNOWN; //init with unknown
			}
		}

		//Fill the board with data
		gameBoard[width - 1][0] = EnumBox.EXIT; //Hunter always start at same place
		hunterBoard[width - 1][0] = EnumBox.HUNTER; //Hunter always start at same place

		fillRandomBox(EnumBox.WUMPUS); //Wumpus can be in any empty box
		fillRandomBox(EnumBox.GOLD); //Gold can be in any empty box

		if (!fillHoles()) {
			createBoard();
		}
	}

	/**
	 * Fill the box of Wumpus and Gold.
	 *
	 * @param box to fill
	 */
	private void fillRandomBox(EnumBox box) {
		int row = getRandomNumber();
		int col = getRandomNumber();
		EnumBox oldBox = gameBoard[row][col];
		if (oldBox == EnumBox.EMPTY) {
			gameBoard[row][col] = box;
		} else {
			fillRandomBox(box);
		}
	}

	/**
	 * Fills the board with holes, checking that is possible to win.
	 */
	private boolean fillHoles() {
		int holesFilled = 0;
		Set<String> triedBoxs = new HashSet<>();

		boolean impossibleBoard = false;

		while (!impossibleBoard && holesFilled < holes) {
			int row = getRandomNumber();
			int col = getRandomNumber();
			String key = row + ";" + col;
			EnumBox oldBox = gameBoard[row][col];
			if (!triedBoxs.contains(key) && oldBox == EnumBox.EMPTY) {

				EnumBox[][] boardCloned = new EnumBox[width][width];
				System.arraycopy(gameBoard, 0, boardCloned, 0, boardCloned.length);

				boardCloned[row][col] = EnumBox.HOLE;

				if (isWinPossible(boardCloned)) {
					gameBoard[row][col] = EnumBox.HOLE;
					holesFilled++;
				} else {
					boardCloned[row][col] = EnumBox.EMPTY;
				}
			}
			triedBoxs.add(key);
			if (triedBoxs.size() == width * width) {
				impossibleBoard = true; //can't fill the board with this data. Try again
			}
		}
		return !impossibleBoard;
	}

	/**
	 * Check if with this "temporal board" is possible to win.
	 *
	 * @param boardCloned temporal board
	 * @return if is possible to win or not with this stage
	 */
	private boolean isWinPossible(EnumBox[][] boardCloned) {
		int actualRow = width - 1;
		int actualCol = 0;
		if (isGoldAdjacent(actualRow, actualCol)) {
			return true;
		}

		List<String> previousMovs = new LinkedList<>();
		previousMovs.add(actualRow + ";" + actualCol);

		int tries = 0;

		boolean[][] boardVisits = new boolean[width][width];
		boardVisits[actualRow][actualCol] = true;
		//up row-1, col
		//right row, col+1
		//down row+1, col
		//left row, col-1
		while (tries < width * 50) {
			boolean up = false;
			boolean right = false;
			boolean down = false;
			boolean left = false;
			//try up
			int nextRow = actualRow - 1;
			int nextCol = actualCol;
			if (nextRow < width && nextCol < width && nextRow >= 0 && nextCol >= 0) {
				EnumBox nextBox = boardCloned[nextRow][nextCol];
				boolean nextBoxVisited = boardVisits[nextRow][nextCol];
				if ((nextBox == EnumBox.EMPTY || nextBox == EnumBox.WUMPUS) && !nextBoxVisited) { //Wumpus and empty are possible options for walk
					if (isGoldAdjacent(nextRow, nextCol)) {
						return true;
					}
					actualRow = nextRow;
					actualCol = nextCol;
					previousMovs.add(actualRow + ";" + actualCol);
					up = true;
				}
			}
			if (!up) {
				//try right
				nextRow = actualRow;
				nextCol = actualCol + 1;
				if (nextRow < width && nextCol < width && nextRow >= 0 && nextCol >= 0) {
					EnumBox nextBox = boardCloned[nextRow][nextCol];
					boolean nextBoxVisited = boardVisits[nextRow][nextCol];
					if ((nextBox == EnumBox.EMPTY || nextBox == EnumBox.WUMPUS) && !nextBoxVisited) {
						if (isGoldAdjacent(nextRow, nextCol)) {
							return true;
						}
						actualRow = nextRow;
						actualCol = nextCol;
						previousMovs.add(actualRow + ";" + actualCol);
						right = true;
					}
				}
				if (!right) {
					//try down
					nextRow = actualRow + 1;
					nextCol = actualCol;
					if (nextRow < width && nextCol < width && nextRow >= 0 && nextCol >= 0) {
						EnumBox nextBox = boardCloned[nextRow][nextCol];
						boolean nextBoxVisited = boardVisits[nextRow][nextCol];
						if ((nextBox == EnumBox.EMPTY || nextBox == EnumBox.WUMPUS) && !nextBoxVisited) {
							if (isGoldAdjacent(nextRow, nextCol)) {
								return true;
							}
							actualRow = nextRow;
							actualCol = nextCol;
							previousMovs.add(actualRow + ";" + actualCol);
							down = true;
						}
					}
					if (!down) {
						//try left
						nextRow = actualRow;
						nextCol = actualCol - 1;
						if (nextRow < width && nextCol < width && nextRow >= 0 && nextCol >= 0) {
							EnumBox nextBox = boardCloned[nextRow][nextCol];
							boolean nextBoxVisited = boardVisits[nextRow][nextCol];
							if ((nextBox == EnumBox.EMPTY || nextBox == EnumBox.WUMPUS) && !nextBoxVisited) {
								if (isGoldAdjacent(nextRow, nextCol)) {
									return true;
								}
								actualRow = nextRow;
								actualCol = nextCol;
								previousMovs.add(actualRow + ";" + actualCol);
								left = true;
							}
						}

						if (!left) { //go previous position
							String[] previousMov = previousMovs.get(previousMovs.size() - 1).split(";");
							actualRow = Integer.parseInt(previousMov[0]);
							actualCol = Integer.parseInt(previousMov[1]);
							if (previousMovs.size() > 1) { //not exit box
								previousMovs.remove(previousMovs.size() - 1);
							}
						}
					}

				}

			}
			tries++;
		}
		return false;

	}

	/**
	 * Check if GOLD is adjacent
	 *
	 * @param row to search
	 * @param col to search
	 * @return if GOLD is near
	 */
	private boolean isGoldAdjacent(int row, int col) {
		return isGoldInBox(row - 1, col) || //up
				isGoldInBox(row, col + 1) || //right
				isGoldInBox(row - 1, col) || //down
				isGoldInBox(row, col - 1); //left
	}

	/**
	 * Check if GOLD box is in this box
	 *
	 * @param row to search
	 * @param col to search
	 * @return if gold is there or not
	 */
	private boolean isGoldInBox(int row, int col) {
		if (row >= width || col >= width || row < 0 || col < 0) { //outside
			return false;
		}
		return gameBoard[row][col] == EnumBox.GOLD;
	}

	/**
	 * Gets a random number between 0 and width - 1
	 *
	 * @return number
	 */
	private int getRandomNumber() {
		return random.nextInt(width);
	}

	/**
	 * Gets the next box. If the boolean is true, move the position. Else, only gets the box.
	 *
	 * @param movePosition yes or not
	 * @return next box
	 */
	EnumBox getNextBox(boolean movePosition) {
		switch (actualOrientation) {
			case NORTH:
				return moveNorth(movePosition);
			case EAST:
				return moveEast(movePosition);
			case SOUTH:
				return moveSouth(movePosition);
			case WEAST:
				return moveWeast(movePosition);
		}
		return null;
	}

	/**
	 * Set the message with the data of the actual box and next box
	 */
	void setAndPrintMessage() {
		EnumBox actualBox = getActualBox();
		EnumBox nextBox = getNextBox(false);
		switch (actualBox) {
			case EXIT:
				if (nextBox == EnumBox.WUMPUS) {
					lastMessage = EnumBox.EXIT_NEAR_WUMPUS.getMessage();
				} else if (nextBox == EnumBox.HOLE) {
					lastMessage = EnumBox.EXIT_NEAR_HOLE.getMessage();
				} else {
					lastMessage = EnumBox.EXIT.getMessage();
				}
				break;
			case EMPTY:
				if (nextBox == EnumBox.WUMPUS) {
					lastMessage = EnumBox.NEAR_WUMPUS.getMessage();
				} else if (nextBox == EnumBox.HOLE) {
					lastMessage = EnumBox.NEAR_HOLE.getMessage();
				} else {
					lastMessage = EnumBox.EMPTY.getMessage();
				}
				break;
			default:
				lastMessage = actualBox.getMessage();
		}
		System.out.println("---------------------------------------------");
		System.out.println("The message in the current box is:");
		System.out.println(lastMessage);
		System.out.println("---------------------------------------------");
	}

	/**
	 * Gets a message with the actual situation of the hunter
	 *
	 * @return message
	 */
	String getSituation() {
		for (EnumBox[] enumBoxes : hunterBoard) System.out.println(Arrays.toString(enumBoxes));

		String situation = "The situation for the hunter is as follows:\n";
		situation += "	Last message was: " + lastMessage + ".\n";
		situation += "	Hunter has " + arrows + " arrows left.\n";
		situation += "	Hunter is in row " + (actualRow + 1) + " and column " + (actualCol + 1) + ", looking " + actualOrientation.name() + ".\n";
		return situation;
	}

	/**
	 * Gets the box after a northward movement.
	 *
	 * @param movePosition if actual row and column must be updated or not
	 * @return box
	 */
	private EnumBox moveNorth(boolean movePosition) {
		int nextRow = actualRow - 1;
		int nextCol = actualCol;
		return moveGeneric(movePosition, nextRow, nextCol);
	}

	/**
	 * Gets the box after a eastward movement.
	 *
	 * @param movePosition if actual row and column must be updated or not
	 * @return box
	 */
	private EnumBox moveEast(boolean movePosition) {
		int nextRow = actualRow;
		int nextCol = actualCol + 1;
		return moveGeneric(movePosition, nextRow, nextCol);
	}

	/**
	 * Gets the box after a southward movement.
	 *
	 * @param movePosition if actual row and column must be updated or not
	 * @return box
	 */
	private EnumBox moveSouth(boolean movePosition) {
		int nextRow = actualRow + 1;
		int nextCol = actualCol;
		return moveGeneric(movePosition, nextRow, nextCol);
	}

	/**
	 * Gets the box after a weastward movement.
	 *
	 * @param movePosition if actual row and column must be updated or not
	 * @return box
	 */
	private EnumBox moveWeast(boolean movePosition) {
		int nextRow = actualRow;
		int nextCol = actualCol - 1;
		return moveGeneric(movePosition, nextRow, nextCol);
	}

	/**
	 * Move a generic position
	 *
	 * @param movePosition if actual row and column must be updated or not
	 * @param nextRow      next box row
	 * @param nextCol      next box col
	 * @return next box
	 */
	private EnumBox moveGeneric(boolean movePosition, int nextRow, int nextCol) {
		EnumBox actualBox = getActualBox();
		if (nextRow < 0 || nextRow >= width || nextCol < 0 || nextCol >= width) {
			lastMessage = EnumBox.WALL.getMessage();
			return actualBox; //no move
		}
		if (movePosition) {
			hunterBoard[actualRow][actualCol] = actualBox;
			this.actualRow = nextRow;
			this.actualCol = nextCol;
			hunterBoard[actualRow][actualCol] = EnumBox.HUNTER;
		}
		return gameBoard[nextRow][nextCol];
	}

	/**
	 * Gets the actual box
	 *
	 * @return box
	 */
	EnumBox getActualBox() {
		return gameBoard[actualRow][actualCol];
	}

	/**
	 * Rotate 90% left
	 */
	void rotateLeft() {
		switch (actualOrientation) {
			case NORTH:
				actualOrientation = EnumOrientation.WEAST;
				break;
			case EAST:
				actualOrientation = EnumOrientation.NORTH;
				break;
			case SOUTH:
				actualOrientation = EnumOrientation.EAST;
				break;
			case WEAST:
				actualOrientation = EnumOrientation.SOUTH;
				break;
		}
	}

	/**
	 * Rotate 90% right
	 */
	void rotateRight() {
		switch (actualOrientation) {
			case NORTH:
				actualOrientation = EnumOrientation.EAST;
				break;
			case EAST:
				actualOrientation = EnumOrientation.SOUTH;
				break;
			case SOUTH:
				actualOrientation = EnumOrientation.WEAST;
				break;
			case WEAST:
				actualOrientation = EnumOrientation.NORTH;
				break;
		}
	}

	/**
	 * Shoot to kill the Wumpus.
	 */
	void shoot() {
		if (arrows - 1 >= 0) {
			if (wumpusInRange()) {
				System.out.println("You have killed the Wumpus!!");
				removeWumpus();
			} else {
				System.out.println("Sorry, I failed. Wumpus not in range.");
			}
			arrows--;
		} else {
			System.out.println("Sorry, I don't have arrows :(");
		}
	}

	/**
	 * Check if wumpus is in range
	 *
	 * @return true or false
	 */
	private boolean wumpusInRange() {
		int actualRowTmp = actualRow;
		int actualColTmp = actualCol;
		String lastMessageTmp = lastMessage;
		EnumBox nextBox;
		while (!lastMessage.equals(EnumBox.WALL.getMessage())) {
			nextBox = getNextBox(true);
			if (nextBox == EnumBox.WUMPUS) {
				actualRow = actualRowTmp;
				actualCol = actualColTmp;
				return true;
			}
		}
		actualRow = actualRowTmp;
		actualCol = actualColTmp;
		lastMessage = lastMessageTmp;
		return false;
	}

	/**
	 * Remove wumpus box
	 */
	private void removeWumpus() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				if (gameBoard[i][j] == EnumBox.WUMPUS) {
					gameBoard[i][j] = EnumBox.EMPTY;
					break;
				}
			}
		}
	}

	/**
	 * Gets the value of board
	 *
	 * @return value of board
	 */
	EnumBox[][] getGameBoard() {
		return gameBoard;
	}

	/**
	 * Get the value of the remaining arrows.
	 *
	 * @return remaining arrows
	 */
	int getArrows() {
		return arrows;
	}
}
