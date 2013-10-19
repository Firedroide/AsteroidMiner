package ch.kanti_wohlen.asteroidminer.powerups;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.entities.Entity;

public abstract class PowerUp extends Entity {
	
	public PowerUp(World world, BodyDef bodyDef, Shape collisionBox) {
		super(world, bodyDef, collisionBox);
	}
	
	public abstract void onPickUp(Player player);
	
	public abstract float getDropFrequency();
}
