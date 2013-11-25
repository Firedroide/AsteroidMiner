package ch.kanti_wohlen.asteroidminer.entities.bars;

import ch.kanti_wohlen.asteroidminer.Textures;

public class HealthBar extends AbstractBar {

	public HealthBar(float maximumHealth) {
		super(Textures.HEALTH_HIGH, Textures.HEALTH_LOW, maximumHealth);
	}
}
