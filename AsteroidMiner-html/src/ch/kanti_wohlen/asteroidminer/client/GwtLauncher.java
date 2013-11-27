package ch.kanti_wohlen.asteroidminer.client;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;
import ch.kanti_wohlen.asteroidminer.GameLauncher;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication implements GameLauncher {

	@Override
	public GwtApplicationConfiguration getConfig() {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(960, 640);
		cfg.fps = 60;
		cfg.antialiasing = false;
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return new AsteroidMiner(this);
	}

	@Override
	public void log(String tag, String message, Throwable exception) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getLogLevel() {
		return 0;
	}

	@Override
	public void onModuleLoad() {
		super.onModuleLoad();
		// TODO: Show Facebook authorization dialog
		// logIn();
	}

	public static final native void alert(String msg) /*-{
		alert(msg);
	}-*/;

	public static final native void logIn() /*-{
		$doc.logIn();
	}-*/;
}
