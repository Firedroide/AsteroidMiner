package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.Laser;
import ch.kanti_wohlen.asteroidminer.entities.asteroids.StoneAsteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionListener implements ContactFilter, ContactListener {

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		Entity e1 = (Entity) fixtureA.getBody().getUserData();
		Entity e2 = (Entity) fixtureB.getBody().getUserData();

		if (e1 instanceof Laser || e2 instanceof Laser) {
			if (e1 instanceof Laser && e2 instanceof StoneAsteroid) {
				contactLaserAsteroid((Laser) e1, (StoneAsteroid) e2);
			} else if (e1 instanceof StoneAsteroid && e2 instanceof Laser) {
				contactLaserAsteroid((Laser) e2, (StoneAsteroid) e1);
			}
			return false;
		}

		return true;
	}

	private void contactLaserAsteroid(Laser laser, StoneAsteroid asteroid) {
		final float x = -MathUtils.sin(laser.getPhysicsBody().getAngle());
		final float y = MathUtils.cos(laser.getPhysicsBody().getAngle());

		final float mass = asteroid.getPhysicsBody().getMass();
		final Vector2 force = new Vector2(x * 4f * mass, y * 4f * mass);
		TaskScheduler.INSTANCE.runTask(new ForceApplier(asteroid, force));

		asteroid.damage(5);
		laser.remove();
	}

	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA() == null || contact.getFixtureB() == null) return;
		Gdx.app.log("DEBUG", "Begin contact " + contact.getFixtureA().toString() + contact.getFixtureB().toString());

	}

	@Override
	public void endContact(Contact contact) {
		if (contact.getFixtureA() == null || contact.getFixtureB() == null) return;
		Gdx.app.log("DEBUG", "End contact " + contact.getFixtureA().toString() + contact.getFixtureB().toString());

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
