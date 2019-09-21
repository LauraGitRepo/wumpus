package es.laura;

/**
 * @author Laura
 * @version 1.0.0
 */
public enum EnumChoice {
	UP("W", "Go up"),
	DOWN("S", "Go down"),
	LEFT("A", "Go left"),
	RIGHT("D", "Go right"),
	ROTATE_LEFT("Q", "Rotate 90º to left"),
	ROTATE_RIGHT("E", "Rotate 90º to right"),
	SHOOT("T", "Shoot forward"),
	EXIT("Y", "Exit (only on exit box)"),
	HELP("H", "Help (all info about the keys)");

	private String key;

	private String descr;

	EnumChoice(String key, String descr) {
		this.key = key;
		this.descr = descr;
	}

	/**
	 * Gets the value of key variable
	 *
	 * @return key value
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets the value of descr variable
	 *
	 * @return descr value
	 */
	public String getDescr() {
		return descr;
	}

	protected static EnumChoice getChoice(String keyValue){
		for(EnumChoice choice: values()){
			if (choice.getKey().equals(keyValue)) {
				return choice;
			}
		}
		return null;
	}

	/**
	 * Gets the information about the choices of the game
	 *
	 * @return text with the choices
	 */
	protected static String getChoices() {
		StringBuilder choices = new StringBuilder();
		for (EnumChoice choice : values()) {
			choices.append(choice.getKey()).append(" -> ").append(choice.getDescr()).append("\n");
		}
		return choices.toString();
	}
}
