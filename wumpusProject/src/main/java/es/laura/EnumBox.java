package es.laura;

/**
 * @author Laura
 * @version 1.0.0
 */
public enum EnumBox {
	EMPTY("This box are empty"),
	UNKNOWN("Unknown"),
	HUNTER("Hunter"),
	WUMPUS("Oh no!! The wumpus X.x"),
	HOLE("NoooOooOoooooo.... (plof)"),
	NEAR_WUMPUS("I think someone needs a shower..."),
	NEAR_HOLE("What a pleasant breeze!"),
	GOLD("I found the gold! It is so sparkly..."),
	EXIT_NEAR_WUMPUS("Exit!! But... I think someone needs a shower..."),
	EXIT_NEAR_HOLE("Exit!! And... What a pleasant breeze!"),
	WALL("Crash!! I can't go through walls..."),
	EXIT("Exit!!");

	private String message;

	EnumBox(String message) {
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
