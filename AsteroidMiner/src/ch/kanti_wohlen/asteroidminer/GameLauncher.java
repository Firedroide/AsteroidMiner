package ch.kanti_wohlen.asteroidminer;

import java.util.List;

public interface GameLauncher {

	public void setHighscore(int newScore);

	public List<Pair<String, Integer>> getHighscores();

	public void postFeedHighscore();

	public void postFeedFriendScoreBeaten();
}
