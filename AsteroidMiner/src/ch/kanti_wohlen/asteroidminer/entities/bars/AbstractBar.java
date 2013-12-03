package ch.kanti_wohlen.asteroidminer.entities.bars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class AbstractBar {

	public static final float MAX_ALPHA = 5f;

	private final Sprite spriteHigh;
	private final Sprite spriteLow;
	private final float maxVal;
	private final float sizeModifier;

	private float alpha;

	public AbstractBar(Sprite highBarSprite, Sprite lowBarSprite, float maximumValue) {
		spriteHigh = highBarSprite;
		spriteLow = lowBarSprite;
		maxVal = maximumValue;
		alpha = 0f;
		sizeModifier = 0.6f + maximumValue / 400f;
	}

	public void render(SpriteBatch batch, float value, Vector2 location) {
		alpha = Math.max(0f, alpha - Gdx.graphics.getDeltaTime());
		render(batch, value, location, alpha);
	}

	public void render(SpriteBatch batch, float value, Vector2 location, float alpha) {
		if (alpha <= 0) return;

		final int xm = Math.round((float) value / maxVal * spriteHigh.getRegionWidth());
		final float xmScl = xm * sizeModifier;
		final float xOff = spriteHigh.getRegionWidth() * ((1f - sizeModifier) * 0.5f);
		final int xn = spriteLow.getRegionWidth() - xm;
		final int wHigh = spriteHigh.getRegionWidth();
		final int xLow = spriteLow.getRegionX();
		final int wLow = spriteLow.getRegionWidth();
		final float yHeight = spriteHigh.getRegionHeight() * sizeModifier;

		spriteHigh.setBounds(location.x + xOff, location.y, xmScl, yHeight);
		spriteHigh.setRegionWidth(xm);
		spriteHigh.draw(batch, Math.min(alpha, 1f));
		spriteHigh.setRegionWidth(wHigh);

		spriteLow.setBounds(location.x + xOff + xmScl, location.y, xn * sizeModifier, yHeight);
		spriteLow.setRegionX(xLow + xm);
		spriteLow.setRegionWidth(xn);
		spriteLow.draw(batch, Math.min(alpha, 1f));
		spriteLow.setRegionX(xLow);
		spriteLow.setRegionWidth(wLow);
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float newAlpha) {
		alpha = newAlpha;
	}

	public void resetAlpha() {
		alpha = MAX_ALPHA;
	}
}
