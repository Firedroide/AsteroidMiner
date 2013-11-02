package ch.kanti_wohlen.asteroidminer.entities.sub;

import ch.kanti_wohlen.asteroidminer.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class HealthBar {

	public static final float HEALTH_BAR_ALPHA_MAX = 5f;
	private final float maxHealth;
	private float healthAlpha;

	public HealthBar(float maximumHealth) {
		maxHealth = maximumHealth;
		healthAlpha = 0f;
	}

	public void render(SpriteBatch batch, float health, Vector2 location) {
		if (!(healthAlpha > 0)) return;

		healthAlpha = Math.max(0f, healthAlpha - Gdx.graphics.getDeltaTime());

		final int xm = Math.round((float) health / maxHealth * Textures.HEALTH_HIGH.getRegionWidth());
		final int xn = Textures.HEALTH_LOW.getRegionWidth() - xm;
		final int wHigh = Textures.HEALTH_HIGH.getRegionWidth();
		final int xLow = Textures.HEALTH_LOW.getRegionX();
		final int wLow = Textures.HEALTH_LOW.getRegionWidth();

		Textures.HEALTH_HIGH.setBounds(location.x, location.y, xm, Textures.HEALTH_HIGH.getHeight());
		Textures.HEALTH_HIGH.setRegionWidth(xm);
		Textures.HEALTH_HIGH.draw(batch, Math.min(healthAlpha, 1f));
		Textures.HEALTH_HIGH.setRegionWidth(wHigh);

		Textures.HEALTH_LOW.setBounds(location.x + xm, location.y, xn, Textures.HEALTH_LOW.getHeight());
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
