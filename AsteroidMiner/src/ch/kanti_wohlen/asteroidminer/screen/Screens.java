package ch.kanti_wohlen.asteroidminer.screen;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;

public final class Screens {
	
	public final GameScreen GAME_SCREEN;
	public final PauseScreen PAUSE_SCREEN;
	
	public Screens(AsteroidMiner asteroidMiner) {
		GAME_SCREEN = new GameScreen(asteroidMiner);
		PAUSE_SCREEN = new PauseScreen(asteroidMiner);
	}
}
