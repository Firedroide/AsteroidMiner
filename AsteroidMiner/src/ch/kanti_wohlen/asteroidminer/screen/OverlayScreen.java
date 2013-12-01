package ch.kanti_wohlen.asteroidminer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;

public abstract class OverlayScreen extends AbstractScreen {

	protected final AsteroidMiner game;
	protected final SpriteBatch batch;
	protected final Sprite overlay;

	public OverlayScreen(AsteroidMiner asteroidMiner) {
		game = asteroidMiner;
		batch = game.getSpriteBatch();

		Pixmap map = new Pixmap(1, 1, Format.RGBA8888);
		map.drawPixel(0, 0, Color.rgba8888(255f, 255f, 255f, 192f));
		overlay = new Sprite(new Texture(map));
		map.dispose();
	}

	@Override
	public void render(float delta) {
		// Draw overlay
		overlay.setPosition(Gdx.graphics.getWidth() * -0.5f, Gdx.graphics.getHeight() * -0.5f);
		overlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setProjectionMatrix(new Matrix4());
		batch.begin();
		overlay.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		overlay.getTexture().dispose();
	}
}