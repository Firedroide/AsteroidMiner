package ch.kanti_wohlen.asteroidminer;

public enum GameMode {
	TIME_2_MIN("2 Minute Game"),
	TIME_5_MIN("5 Minute Game"),
	ENDLESS("Endless Mode");

	private final String name;

	private GameMode(String modeName) {
		name = modeName;
	}

	public String getName() {
		return name;
	}
}
