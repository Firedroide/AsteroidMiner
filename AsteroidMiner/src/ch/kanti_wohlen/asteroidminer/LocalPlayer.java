package ch.kanti_wohlen.asteroidminer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

public class LocalPlayer implements Player {

	private final LocalInput input;
	private final SpaceShip ship;
	private final int id;
	private int score;

	public LocalPlayer(World w) {
		final float yDist = 0.6f * Gdx.graphics.getHeight() * Entity.PIXEL_TO_BOX2D;
		final Vector2 loc = new Vector2(0f, -yDist);
		final Vector2 vel = new Vector2(0f, SpaceShip.LINEAR_DAMPING * yDist);
		ship = new SpaceShip(w, this, loc, vel);
		id = 0;
		score = 0;
		input = new LocalInput();
	}

	@Override
	public SpaceShip getSpaceShip() {
		return ship;
	}

	@Override
	public int getPlayerNumber() {
		return id;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public void setScore(int newScore) {
		if (newScore < 0) return;
		score = newScore;
	}

	@Override
	public void addScore(int difference) {
		score += difference;
	}

	@Override
	public void subtractScore(int difference) {
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
