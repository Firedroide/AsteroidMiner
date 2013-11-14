package ch.kanti_wohlen.asteroidminer.screen;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;

public final class Screens {

	public final PauseScreen PAUSE_SCREEN;
	public final MenuScreen MENU_SCREEN;

	public Screens(AsteroidMiner asteroidMiner) {
		PAUSE_SCREEN = new PauseScreen(asteroidMiner);
		MENU_SCREEN = new MenuScreen(asteroidMiner);
	}
}
