package ch.kanti_wohlen.asteroidminer;

import com.badlogic.gdx.utils.Disposable;

import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

public abstract class Player implements Disposable {
	
	private final SpaceShip ship;
	private long score;
	private final int id;
	
	public Player(SpaceShip spaceShip, int playerID) {
		ship = spaceShip;
		id = playerID;
		score = 0;
	}
	
	@Override
	public void dispose() {
		ship.remove();
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
	
	public abstract void doInput();
}
