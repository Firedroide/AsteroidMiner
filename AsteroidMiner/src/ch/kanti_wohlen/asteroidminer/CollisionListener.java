package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.Laser;
import ch.kanti_wohlen.asteroidminer.entities.asteroids.StoneAsteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
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

		asteroid.getPhysicsBody().setLinearVelocity(x * 10, y * 10);
		Body body = asteroid.getPhysicsBody();
		final float mass = body.getMass();
		body.applyForceToCenter(x * 160f * mass, y * 160f * mass);
		System.out.println("Applied force to Asteroid");
		// laser.remove();
	}

	@Override
	public void beginContact(Contact contact) {
		Gdx.app.log("DEBUG", "Begin contact " + contact.getFixtureA().toString() + contact.getFixtureB().toString());
		
	}

	@Override
	public void endContact(Contact contact) {
		Gdx.app.log("DEBUG", "End contact " + contact.getFixtureA().toString() + contact.getFixtureB().toString());
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		//Gdx.app.log("DEBUG", "Pre-Solve contact " + contact.getFixtureA().toString() + contact.getFixtureB().toString());
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		//Gdx.app.log("DEBUG", "Post-Solve contact " + contact.getFixtureA().toString() + contact.getFixtureB().toString());
		
	}
}
