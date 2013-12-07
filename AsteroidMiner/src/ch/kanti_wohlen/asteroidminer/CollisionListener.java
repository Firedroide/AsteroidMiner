package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.entities.Damageable;
import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.EntityType;
import ch.kanti_wohlen.asteroidminer.entities.Laser;
import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;
import ch.kanti_wohlen.asteroidminer.powerups.PowerUp;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionListener implements ContactListener {

	private static final float JOULES_PER_HEALTH = 2000f;

	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA() == null || contact.getFixtureB() == null) return;
		//Gdx.app.log("DEBUG", "Begin contact " + contact.getFixtureA().toString() + contact.getFixtureB().toString());

		Entity e1 = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity e2 = (Entity) contact.getFixtureB().getBody().getUserData();

		entitySwitch(e1, e2);
		entitySwitch(e2, e1);
	}

	private void entitySwitch(Entity e1, Entity e2) {
		switch (e1.getType()) {
		case LASER:
			if (e2.getType() == EntityType.ASTEROID) {
				Laser laser = (Laser) e1;
				contactLaserAsteroid(laser, e2);
			}
			break;
		case SPACESHIP:
			SpaceShip ship = (SpaceShip) e1;
			switch (e2.getType()) {
			case ASTEROID:
				collisionDamage(ship, (Damageable) e2);
				break;
			case POWER_UP:
				PowerUp powerUp = (PowerUp) e2;
				powerUp.onPickUp(ship.getPlayer());
				powerUp.remove();
				break;
			default:
				break;
			}
			break;
		case ASTEROID:
			switch (e2.getType()) {
			case ASTEROID:
				collisionDamage((Damageable) e1, (Damageable) e2);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	private void contactLaserAsteroid(Laser laser, Entity asteroid) {
		final float x = -MathUtils.sin(laser.getPhysicsBody().getAngle());
		final float y = MathUtils.cos(laser.getPhysicsBody().getAngle());

		final float mass = asteroid.getPhysicsBody().getMass();
		final Vector2 force = new Vector2(x * 4f * mass, y * 4f * mass);
		TaskScheduler.INSTANCE.runTask(new ForceApplier(asteroid, force));

		if (asteroid instanceof Damageable) {
			((Damageable) asteroid).damage(laser.getDamage());
		}
		laser.remove();
	}

	private void collisionDamage(Damageable e1, Damageable e2) {
		final float v = e1.getPhysicsBody().getLinearVelocity().sub(e2.getPhysicsBody().getLinearVelocity()).len2();
		final float m1 = e1.getPhysicsBody().getMass();
		final float m2 = e2.getPhysicsBody().getMass();

		final float eDeformation = (m1 * m2 * v) / (2 * (m1 + m2));
		final int dmg = (int) (eDeformation / JOULES_PER_HEALTH);

		e1.damage(dmg);
		e2.damage(dmg);
		//Gdx.app.log("Collision", "Deformation energy: " + String.valueOf(eDeformation)
		//		+ ", Resulting damage: " + String.valueOf(dmg));
	}

	@Override
	public void endContact(Contact contact) {
		if (contact.getFixtureA() == null || contact.getFixtureB() == null) return;
		//Gdx.app.log("DEBUG", "End contact " + contact.getFixtureA().toString() + contact.getFixtureB().toString());

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// Gdx.app.log("DEBUG", "Pre-Solve contact " + contact.getFixtureA().toString() + contact.getFixtureB().toString());

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// Gdx.app.log("DEBUG", "Post-Solve contact " + contact.getFixtureA().toString() + contact.getFixtureB().toString());

	}

	private class ForceApplier implements Runnable {

		final Body physicsBody;
		final Vector2 forceApplied;

		public ForceApplier(Entity entity, Vector2 force) {
			physicsBody = entity.getPhysicsBody();
			forceApplied = force;
		}

		@Override
		public void run() {
			physicsBody.applyForceToCenter(forceApplied, true);
		}
	}
}
