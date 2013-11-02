package ch.kanti_wohlen.asteroidminer;

import com.badlogic.gdx.physics.box2d.World;

import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

public class LocalPlayer implements Player {

	private final Input input;
	private final SpaceShip ship;
	private long score;
	private final int id;

	public LocalPlayer(AsteroidMiner game, World w) {
		ship = new SpaceShip(w, this);
		id = 0;
		score = 0;
		input = new Input(game);
	}

	public SpaceShip getSpaceShip() {
		return ship;
	}

	public int getPlayerNumber() {
		return id;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long newScore) {
		if (newScore < 0) return;
		score = newScore;
	}

	public void addScore(long difference) {
		score += difference;
	}

	public void subtractScore(long difference) {
		score -= difference;
	}

	@Override
	public void doInput() {
		input.onGameRunning(this);
	}

	@Override
	public void dispose() {
		ship.remove();
	}
}
