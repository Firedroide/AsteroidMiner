package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.screen.GameScreen;

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
	
	private float height;
	private float width;
	private float backgroundU2;
	private float backgroundV2;
	
	@Override
	public void create() {
		Textures.load(); // TODO: Loading system.
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		backgroundU2 = width / Textures.BACKGROUND.getWidth();
		backgroundV2 = height / Textures.BACKGROUND.getHeight();
		
		fpsLogger = new FPSLogger();
		camera = new OrthographicCamera(width, height);
		camera.translate(-width / 2f, -height / 2f);
		camera.update();
		batch = new SpriteBatch();
		
		setScreen(new GameScreen(this));
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
		//batch.setProjectionMatrix(camera.combined);
		batch.begin();
		drawBackground();
		super.render();
		batch.end();
	}
	
	@Override
	public void resize(int width, int height) {}
	
	@Override
	public void pause() {}
	
	@Override
	public void resume() {}
	
	public SpriteBatch getSpriteBatch() {
		return batch;
	}
	
	private void drawBackground() {
		batch.draw(Textures.BACKGROUND.getTexture(), 0f, 0f, width, height, 0f, 0f, backgroundU2, backgroundV2);
	}
}
