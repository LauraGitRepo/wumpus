package es.laura;

/**
 * @author Laura
 * @version 1.0.0
 */
public enum EnumCell {
	EMPTY("This cell are empty"),
	WUMPUS("Oh no!! The wumpus X.x"),
	HOLE("NoooOooOoooooo.... (plof)"),
	NEAR_WUMPUS("I think someone needs a shower..."),
	NEAR_HOLE("What a pleasant breeze!"),
	NEAR_HOLE_WUMPUS("I'm confused. There is a nice breeze but at the same time it smells horrible ..."),
	GOLD("I found the gold! It is so sparkly..."),
	EXIT_NEAR_WUMPUS("Exit!! But... I think someone needs a shower..."),
	EXIT_NEAR_HOLE("Exit!! And... What a pleasant breeze!"),
	EXIT_NEAR_HOLE_WUMPUS("Exit!! And... I'm confused. There is a nice breeze but at the same time it smells horrible ..."),
	EXIT("Exit!!");

	private String message;

	EnumCell(String message) {
		this.message = message;
	}

	/**
	 * Gets the value of message
	 *
	 * @return value of message
	 */
	public String getMessage() {
		return message;
	}
}
