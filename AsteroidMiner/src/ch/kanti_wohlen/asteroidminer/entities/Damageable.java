package ch.kanti_wohlen.asteroidminer.entities;

public interface Damageable {

	public int getHealth();

	public void setHealth(int newHealth);

	public void heal(int healingAmoung);

	public void damage(int damageAmount);

	public void kill();
}
