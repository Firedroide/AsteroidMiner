package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.Player;

import com.badlogic.gdx.physics.box2d.Body;

public interface Damageable {

	public Body getPhysicsBody();

	public int getHealth();

	public void setHealth(int newHealth);

	public void heal(int healingAmoung);

	public void damage(int damageAmount, Player player, float scoreMultiplier);

	public void kill();
}
