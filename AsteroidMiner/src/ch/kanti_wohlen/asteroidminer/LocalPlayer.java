package ch.kanti_wohlen.asteroidminer;

import com.badlogic.gdx.physics.box2d.World;

import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

public class LocalPlayer extends Player {
	
	private final Input input;
	
	public LocalPlayer(AsteroidMiner game, World w) {
		super(new SpaceShip(w), 0);
		input = new Input(game);
	}
	
	@Override
	public void doInput() {
		input.onGameRunning(this);
	}
}
