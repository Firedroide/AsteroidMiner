package ch.kanti_wohlen.asteroidminer.powerups;

public enum PowerUpType {
	HEALTH(2f),
	SHIELD(3f),
	MOVEMENT_SPEED(4f),
	FIRING_SPEED(3.5f),
	FIRING_DAMAGE(3f),
	BOMB(1f);

	private final float dropChance;

	private PowerUpType(float chance) {
		dropChance = chance;
	}

	public float getDropChance() {
		return dropChance;
	}
}
