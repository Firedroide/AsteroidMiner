package ch.kanti_wohlen.asteroidminer.entities;

import com.badlogic.gdx.physics.box2d.Body;

public interface Damageable {

	public Body getPhysicsBody();

	public int getHealth();

	public void setHealth(int newHealth);

	public void heal(int healingAmoung);

	public void damage(int damageAmount);

	public void kill();
}
