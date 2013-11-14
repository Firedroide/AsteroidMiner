package ch.kanti_wohlen.asteroidminer;

import com.badlogic.gdx.physics.box2d.World;

import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

public class LocalPlayer implements Player {

	private final LocalInput input;
	private final SpaceShip ship;
	private long score;
	private final int id;

	public LocalPlayer(AsteroidMiner main, World w) {
		ship = new SpaceShip(w, this);
		id = 0;
		score = 0;
		input = new LocalInput(main);
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

	public LocalInput getInput() {
		return input;
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
