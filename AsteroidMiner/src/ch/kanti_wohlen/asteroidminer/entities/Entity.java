package ch.kanti_wohlen.asteroidminer.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Entity {
	
	protected static final float BOX2D_TO_PIXEL = 10f;
	protected static final float PIXEL_TO_BOX2D = 0.1f;
	
	private final Body body;
	private boolean removed;
	
	public Entity(World world, BodyDef bodyDef, Shape collisionBox) {
		body = world.createBody(bodyDef);
		body.setUserData(this);
		if (collisionBox != null) {
			body.createFixture(collisionBox, 1f);
			collisionBox.dispose();
		}
	}
	
	public Body getPhysicsBody() {
		return body;
	}
	
	public boolean remove() {
		if (isRemoved()) return false;
		removed = true;
		return true;
	}
	
	public boolean isRemoved() {
		return removed;
	}
	
	public abstract void render(SpriteBatch batch);
}
