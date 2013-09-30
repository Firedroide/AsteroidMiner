package ch.kanti_wohlen.asteroidminer.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class SpaceShip extends Entity {
	
	public SpaceShip(World world, BodyDef bodyDef) {
		super(world, bodyDef);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		
	}
	
	@Override
	public boolean isRemoved() {
		return false;
	}
	
	@Override
	public float getFriction() {
		return 0.4f;
	}
}
