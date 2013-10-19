package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.entities.Asteroid;
import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.Laser;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class CollisionListener implements ContactFilter {

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		Entity e1 = (Entity) fixtureA.getBody().getUserData();
		Entity e2 = (Entity) fixtureB.getBody().getUserData();

		if (e1 instanceof Laser || e2 instanceof Laser) {
			if (e1 instanceof Laser && e2 instanceof Asteroid) {
				contactLaserAsteroid((Laser) e1, (Asteroid) e2);
			} else if (e1 instanceof Asteroid && e2 instanceof Laser) {
				contactLaserAsteroid((Laser) e2, (Asteroid) e1);
			}
			return false;
		}

		return true;
	}

	private void contactLaserAsteroid(Laser laser, Asteroid asteroid) {
		final float x = -MathUtils.sin(laser.getPhysicsBody().getAngle());
		final float y = MathUtils.cos(laser.getPhysicsBody().getAngle());

		asteroid.getPhysicsBody().setLinearVelocity(x * 10, y * 10);
		Body body = asteroid.getPhysicsBody();
		final float mass = body.getMass();
		body.applyForceToCenter(x * 160f * mass, y * 160f * mass);
		System.out.println("Applied force to Asteroid");
		// laser.remove();
	}
}
