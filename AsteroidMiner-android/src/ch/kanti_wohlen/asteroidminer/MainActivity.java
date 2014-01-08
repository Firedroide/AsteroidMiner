package ch.kanti_wohlen.asteroidminer;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication implements GameLauncher {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.hideStatusBar = true;
		cfg.useGL20 = false;

		initialize(new AsteroidMiner(this, 30), cfg);
	}

	@Override
	public void setHighscore(int newScore) {}

	@Override
	public List<Pair<String, Integer>> getHighscores() {
		return Collections.emptyList();
	}

	@Override
	public void postFeedHighscore() {}

	@Override
	public void postFeedFriendScoreBeaten() {}
}
