package ch.kanti_wohlen.asteroidminer.entities.sub;

import ch.kanti_wohlen.asteroidminer.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class HealthBar {

	public static final float HEALTH_BAR_ALPHA_MAX = 5f;
	private final float maxHealth;
	private final float sizeModifier;
	private float healthAlpha;

	public HealthBar(float maximumHealth) {
		maxHealth = maximumHealth;
		healthAlpha = 0f;
		sizeModifier = 0.6f + maximumHealth / 200f;
	}

	public void render(SpriteBatch batch, float health, Vector2 location) {
		if (!(healthAlpha > 0)) return;

		healthAlpha = Math.max(0f, healthAlpha - Gdx.graphics.getDeltaTime());

		final int xm = Math.round((float) health / maxHealth * Textures.HEALTH_HIGH.getRegionWidth());
		final float xmScl = xm * sizeModifier;
		final float xOff = Textures.HEALTH_HIGH.getRegionWidth() * ((1f - sizeModifier) * 0.5f);
		final int xn = Textures.HEALTH_LOW.getRegionWidth() - xm;
		final int wHigh = Textures.HEALTH_HIGH.getRegionWidth();
		final int xLow = Textures.HEALTH_LOW.getRegionX();
		final int wLow = Textures.HEALTH_LOW.getRegionWidth();
		final float yHeight = Textures.HEALTH_HIGH.getRegionHeight() * sizeModifier;

		Textures.HEALTH_HIGH.setBounds(location.x + xOff, location.y, xmScl, yHeight);
		Textures.HEALTH_HIGH.setRegionWidth(xm);
		Textures.HEALTH_HIGH.draw(batch, Math.min(healthAlpha, 1f));
		Textures.HEALTH_HIGH.setRegionWidth(wHigh);

		Textures.HEALTH_LOW.setBounds(location.x + xOff + xmScl, location.y, xn * sizeModifier, yHeight);
		Textures.HEALTH_LOW.setRegionX(xLow + xm);
		Textures.HEALTH_LOW.setRegionWidth(xn);
		Textures.HEALTH_LOW.draw(batch, Math.min(healthAlpha, 1f));
		Textures.HEALTH_LOW.setRegionX(xLow);
		Textures.HEALTH_LOW.setRegionWidth(wLow);
	}

	public void resetAlpha() {
		healthAlpha = HEALTH_BAR_ALPHA_MAX;
	}
}
