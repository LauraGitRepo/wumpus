package es.laura;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Laura
 * @version 1.0.0
 */
public class WumpusTest {

	private Board board;

	private int width = 5;

	private int holes = 5;

	private int arrows = 5;

	@Before
	public void init() {
		board = new Board(width, holes, arrows);
	}

	@Test
	public void doWidthTest() {
		EnumBox[][] gameBoard = board.getGameBoard();
		Assert.assertEquals(width, gameBoard.length);
	}

	@Test
	public void doCountTest() {

		EnumBox[][] gameBoard = board.getGameBoard();

		int holesCount = 0;
		int goldCount = 0;
		int wumpusCount = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				switch (gameBoard[i][j]) {
					case WUMPUS:
						wumpusCount++;
						break;
					case GOLD:
						goldCount++;
						break;
					case HOLE:
						holesCount++;
						break;
				}
			}
		}
		Assert.assertEquals(holes, holesCount);
		Assert.assertEquals(1, goldCount);
		Assert.assertEquals(1, wumpusCount);
	}

	@Test
	public void doShootTest() {

		board.shoot();
		board.shoot();
		board.shoot();
		Assert.assertEquals(arrows - 3, board.getArrows());

		board.shoot();
		board.shoot();
		board.shoot();
		board.shoot();
		board.shoot();
		board.shoot();
		board.shoot();
		board.shoot();
		board.shoot();
		Assert.assertEquals(0, board.getArrows());

	}
}
