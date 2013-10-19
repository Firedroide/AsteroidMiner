package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.screen.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AsteroidMiner extends Game {

	private FPSLogger fpsLogger;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Screens screens;

	@Override
	public void create() {
		Textures.load(); // TODO: Loading system.

		float height = Gdx.graphics.getHeight();
		float width = Gdx.graphics.getWidth();

		fpsLogger = new FPSLogger();
		camera = new OrthographicCamera(width, height);
		camera.translate(-width / 2f, -height / 2f);
		camera.update();
		batch = new SpriteBatch();

		screens = new Screens(this);
		setScreen(screens.GAME_SCREEN);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {
		// Log FPS
		fpsLogger.log();

		// GL: Clear color buffer --> "Reset" Image
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// GL: Fill everything with blackness (no redness or whiteness involved)
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);

		// Render the currently active screen.
		// batch.setProjectionMatrix(camera.combined);
		batch.begin();
		super.render();
		batch.end();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public Screens getScreens() {
		return screens;
	}
}
