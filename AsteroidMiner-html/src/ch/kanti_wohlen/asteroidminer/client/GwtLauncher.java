package ch.kanti_wohlen.asteroidminer.client;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(960, 640);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new AsteroidMiner();
	}

	@Override
	public void log(String tag, String message, Throwable exception) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getLogLevel() {
		return 0;
	}
}
