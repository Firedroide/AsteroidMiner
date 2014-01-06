package ch.kanti_wohlen.asteroidminer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import com.badlogic.gdx.Preferences;

import com.badlogic.gdx.Gdx;
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

	@Override
	public void setHighscore(int newScore) {
		Preferences prefs = Gdx.app.getPreferences("scores");
		Map<String, ?> map = prefs.get();
		prefs.putInteger(String.valueOf(System.currentTimeMillis()), newScore);
		prefs.flush();

		// Remove lowest entries when 10 or more highscores exist
		while (map.size() >= 11) {
			String keyMin = null;
			int scoreMin = Integer.MAX_VALUE;
			for (String key : map.keySet()) {
				int score = Integer.valueOf(String.valueOf(map.get(key)));
				if (score < scoreMin) {
					keyMin = key;
					scoreMin = score;
				}
			}
			if (keyMin != null) {
				prefs.remove(keyMin);
				map = prefs.get();
			}
		}
		prefs.flush();
	}

	@Override
	public List<Pair<String, Integer>> getHighscores() {
		List<Pair<String, Integer>> returnValues = new ArrayList<Pair<String, Integer>>();
		List<Map.Entry<String, ?>> scoreList = new ArrayList<Map.Entry<String, ?>>(
				Gdx.app.getPreferences("scores").get().entrySet());

		Collections.sort(scoreList, new Comparator<Entry<String, ?>>() {

			@Override
			public int compare(Entry<String, ?> o1, Entry<String, ?> o2) {
				Integer v1 = Integer.valueOf(String.valueOf(o1.getValue()));
				Integer v2 = Integer.valueOf(String.valueOf(o2.getValue()));
				return v2.compareTo(v1);
			}
		});

		for (int i = 0; i < scoreList.size(); ++i) {
			final Entry<String, ?> entry = scoreList.get(i);
			final Integer score = Integer.valueOf(String.valueOf(entry.getValue()));
			returnValues.add(new Pair<String, Integer>(String.valueOf(i + 1), score));
		}
		return returnValues;
	}

	@Override
	public void postFeedHighscore() {}

	@Override
	public void postFeedFriendScoreBeaten() {}
}
