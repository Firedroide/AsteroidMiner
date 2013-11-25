package ch.kanti_wohlen.asteroidminer.entities.bars;

import ch.kanti_wohlen.asteroidminer.Textures;

public class ShieldBar extends AbstractBar {

	public ShieldBar(float maximumShield) {
		super(Textures.SHIELD_HIGH, Textures.SHIELD_LOW, maximumShield);
	}
}
