package ch.kanti_wohlen.asteroidminer;

import com.badlogic.gdx.utils.Disposable;

import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

public interface Player extends Disposable {

	public SpaceShip getSpaceShip();

	public int getPlayerNumber();

	public int getScore();

	public void doInput();
}
