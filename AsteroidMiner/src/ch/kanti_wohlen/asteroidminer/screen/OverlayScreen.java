package ch.kanti_wohlen.asteroidminer.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;

public abstract class OverlayScreen extends AbstractScreen {

	protected static final Matrix4 UNITY_MATRIX = new Matrix4().setToOrtho2D(0f, 0f, 1f, 1f);

	protected final AsteroidMiner game;
	protected final SpriteBatch batch;
	protected final Sprite overlay;

	public OverlayScreen(Color color) {
		game = AsteroidMiner.INSTANCE;
		batch = game.getSpriteBatch();

		Pixmap map = new Pixmap(1, 1, Format.RGBA8888);
		map.drawPixel(0, 0, Color.rgba8888(color));
		overlay = new Sprite(new Texture(map));
		map.dispose();
	}

	@Override
	public void render(float delta) {
		// Draw overlay
		batch.setProjectionMatrix(UNITY_MATRIX);
		batch.begin();
		overlay.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		overlay.getTexture().dispose();
	}
}
