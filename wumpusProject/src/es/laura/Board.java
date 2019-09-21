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
	 * Number of holes in the board (Max 2*widtch)
	 */
	private int holes;

	/**
	 * Number of arrows of the hunter
	 */
	private int arrows;

	/**
	 * Board with all the cell data
	 */
	private EnumCell[][] board;

	/**
	 * Actual cell of the game
	 */
	private EnumCell actualCell = EnumCell.EXIT;

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
	public Board(int width, int holes, int arrows) {
		this.width = width;
		this.holes = holes;
		this.arrows = arrows;
		random = new Random();
		createBoard();
		for (EnumCell[] enumCells : board) System.out.println(Arrays.toString(enumCells));
	}

	/**
	 * Create a random but possible board
	 */
	private void createBoard() {
		board = new EnumCell[width][width];

		//Init with empty cells
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				board[i][j] = EnumCell.EMPTY;
			}
		}

		//Fill the board with data
		board[width - 1][0] = EnumCell.EXIT; //Hunter always start at same place

		fillRandomCell(EnumCell.WUMPUS); //Wumpus can be in any empty cell
		fillRandomCell(EnumCell.GOLD); //Gold can be in any empty cell

		if (!fillHoles()) {
			createBoard();
		}
	}

	/**
	 * Fill the cell of Wumpus and Gold.
	 *
	 * @param cell to fill
	 */
	private void fillRandomCell(EnumCell cell) {
		int row = getRandomNumber();
		int col = getRandomNumber();
		EnumCell oldCell = board[row][col];
		if (oldCell == EnumCell.EMPTY) {
			board[row][col] = cell;
		} else {
			fillRandomCell(cell);
		}
	}

	/**
	 * Fills the board with holes, checking that is possible to win.
	 */
	private boolean fillHoles() {
		int holesFilled = 0;
		Set<String> triedCells = new HashSet<>();

		boolean impossibleBoard = false;

		while (!impossibleBoard && holesFilled < holes) {
			int row = getRandomNumber();
			int col = getRandomNumber();
			String key = row + ";" + col;
			EnumCell oldCell = board[row][col];
			if (!triedCells.contains(key) && oldCell == EnumCell.EMPTY) {

				EnumCell[][] boardCloned = new EnumCell[width][width];
				System.arraycopy(board, 0, boardCloned, 0, boardCloned.length);

				boardCloned[row][col] = EnumCell.HOLE;

				if (isWinPossible(boardCloned)) {
					board[row][col] = EnumCell.HOLE;
					holesFilled++;
				} else {
					boardCloned[row][col] = EnumCell.EMPTY;
				}
			}
			triedCells.add(key);
			if (triedCells.size() == width * width) {
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
	private boolean isWinPossible(EnumCell[][] boardCloned) {
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
				EnumCell nextCell = boardCloned[nextRow][nextCol];
				boolean nextCellVisited = boardVisits[nextRow][nextCol];
				if ((nextCell == EnumCell.EMPTY || nextCell == EnumCell.WUMPUS) && !nextCellVisited) { //Wumpus and empty are possible options for walk
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
					EnumCell nextCell = boardCloned[nextRow][nextCol];
					boolean nextCellVisited = boardVisits[nextRow][nextCol];
					if ((nextCell == EnumCell.EMPTY || nextCell == EnumCell.WUMPUS) && !nextCellVisited) {
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
						EnumCell nextCell = boardCloned[nextRow][nextCol];
						boolean nextCellVisited = boardVisits[nextRow][nextCol];
						if ((nextCell == EnumCell.EMPTY || nextCell == EnumCell.WUMPUS) && !nextCellVisited) {
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
							EnumCell nextCell = boardCloned[nextRow][nextCol];
							boolean nextCellVisited = boardVisits[nextRow][nextCol];
							if ((nextCell == EnumCell.EMPTY || nextCell == EnumCell.WUMPUS) && !nextCellVisited) {
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
							if (previousMovs.size() > 1) { //not exit cell
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
		return isGoldInCell(row - 1, col) || //up
				isGoldInCell(row, col + 1) || //right
				isGoldInCell(row - 1, col) || //down
				isGoldInCell(row, col - 1); //left
	}

	/**
	 * Check if GOLD cell is in this cell
	 *
	 * @param row to search
	 * @param col to search
	 * @return if gold is there or not
	 */
	private boolean isGoldInCell(int row, int col) {
		if (row >= width || col >= width || row < 0 || col < 0) { //outside
			return false;
		}
		return board[row][col] == EnumCell.GOLD;
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
	 * Set the value of enumCell
	 *
	 * @param actualCell value to set
	 */
	public void setActualCell(EnumCell actualCell) {
		this.actualCell = actualCell;
	}

	/**
	 * Gets the actual cell
	 *
	 * @return cell
	 */
	public EnumCell getActualCell() {
		return actualCell;
	}

	/**
	 * Gets a message with the actual situation of the hunter
	 *
	 * @return message
	 */
	protected String getSituation() {
		String situation = "The situation for you is as follows:\n";
		situation += "	Last message was: " + actualCell.getMessage() + ".\n";
		situation += "	You have " + arrows + " arrows left.\n";
		return situation;
	}

}
