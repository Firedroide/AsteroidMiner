package ch.kanti_wohlen.asteroidminer.screen;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;
import ch.kanti_wohlen.asteroidminer.TaskScheduler;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class ScreenSwitchManager implements Runnable {

	private static final float OVERLAY_SECONDS = 2.4f;
	private static final float OVERLAY_HALFTIME = OVERLAY_SECONDS / 2f;
	private static final Matrix4 UNITY_MATRIX = new Matrix4().setToOrtho2D(0f, 0f, 1f, 1f);

	private final AsteroidMiner main;
	private final Sprite overlay;
	private final Screen screen;
	private final int changeTick;

	private int tick = 0;
	private float alpha;

	public ScreenSwitchManager(AsteroidMiner asteroidMiner, Screen newScreen, Color overlayColor) {
		main = asteroidMiner;
		screen = newScreen;

		final Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.drawPixel(0, 0, Color.rgba8888(overlayColor));
		overlay = new Sprite(new Texture(pixmap));

		final int executions = (int) (OVERLAY_SECONDS * TaskScheduler.TICKS_PER_SECOND);
		changeTick = executions / 2;
		TaskScheduler.INSTANCE.runTaskMultipleTimes(this, 0, 1, executions);
	}

	@Override
	public void run() {
		++tick;
		alpha = Math.max(0f, 1f - Math.abs((tick * TaskScheduler.TICK_TIME) - OVERLAY_HALFTIME) / OVERLAY_HALFTIME);

		if (tick == changeTick) {
			main.setScreen(screen);
		}
	}

	public void render(SpriteBatch batch) {
		if (alpha == 0f) return;

		Matrix4 oldMatrix = batch.getProjectionMatrix();
		batch.setProjectionMatrix(UNITY_MATRIX);
		batch.enableBlending();

		batch.begin();
		overlay.draw(batch, alpha);
		batch.end();

		batch.disableBlending();
		batch.setProjectionMatrix(oldMatrix);
	}
}
