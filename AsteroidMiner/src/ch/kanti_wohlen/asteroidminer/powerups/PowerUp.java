package ch.kanti_wohlen.asteroidminer.powerups;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.entities.Entity;

public abstract class PowerUp extends Entity {

	protected static final float PICKUP_RADIUS = 0.5f;

	public PowerUp(World world, Vector2 position) {
		super(world, createBodyDef(position), createCircle());
	}

	private static BodyDef createBodyDef(Vector2 position) {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.set(position);
		bodyDef.angularVelocity = 1f;

		return bodyDef;
	}

	private static FixtureDef createCircle() {
		final FixtureDef fixture = new FixtureDef();
		fixture.density = 0f;
		fixture.isSensor = true;
		final CircleShape cs = new CircleShape();
		cs.setRadius(PICKUP_RADIUS);
		fixture.shape = cs;
		return fixture;
	}

	public abstract void onPickUp(Player player);

	public abstract float getDropFrequency();
}
