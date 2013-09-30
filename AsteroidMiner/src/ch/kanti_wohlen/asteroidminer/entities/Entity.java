package ch.kanti_wohlen.asteroidminer.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Entity {
	
	private final Body body;
	
	public Entity(World world, BodyDef bodyDef) {
		body = world.createBody(bodyDef);
		body.setUserData(this);
	}
	
	public Body getPhysicsBody() {
		return body;
	}
	
	public abstract void render(SpriteBatch batch);
	
	public abstract boolean isRemoved();
	
	public abstract float getFriction();
}
