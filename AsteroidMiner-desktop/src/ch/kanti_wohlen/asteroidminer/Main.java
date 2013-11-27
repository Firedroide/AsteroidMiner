package ch.kanti_wohlen.asteroidminer;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main implements GameLauncher {

	public static void main(String[] args) {
		new Main().start();
	}

	private void start() {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "AsteroidMiner";
		cfg.useGL20 = false;
		cfg.width = 960;
		cfg.height = 640;
		cfg.foregroundFPS = 60;
		cfg.backgroundFPS = -1;

		new LwjglApplication(new AsteroidMiner(this), cfg);
	}
}
