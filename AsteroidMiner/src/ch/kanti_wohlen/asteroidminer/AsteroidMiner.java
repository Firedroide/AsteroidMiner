package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.audio.MusicPlayer;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer;
import ch.kanti_wohlen.asteroidminer.screen.GameScreen;
import ch.kanti_wohlen.asteroidminer.screen.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AsteroidMiner extends Game {

	private GameScreen game;
	private FPSLogger fpsLogger;
	private SpriteBatch batch;
	private Screens screens;

	@Override
	public void create() {
		Textures.load(); // TODO: Loading system.
		SoundPlayer.loadSounds();
		MusicPlayer.load();

		fpsLogger = new FPSLogger();
		batch = new SpriteBatch();

		game = new GameScreen(this);
		screens = new Screens(this);
		setScreen(screens.MENU_SCREEN);
		MusicPlayer.start();
	}

	@Override
	public void dispose() {
		game.dispose();
		screens.MENU_SCREEN.dispose();
		screens.PAUSE_SCREEN.dispose();
		batch.dispose();

		SoundPlayer.dispose();
		MusicPlayer.dispose();
	}

	@Override
	public void render() {
		// GL: Clear color buffer --> "Reset" Image
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// GL: Fill everything with darkness (no redness or whiteness involved)
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);

		// Render the game.
		game.render();

		// Render the currently active screen, if any.
		super.render();

		// If the game is not paused, let the game tick.
		if (getScreen() != screens.PAUSE_SCREEN) {
			game.tick(Gdx.graphics.getDeltaTime());

			// Log FPS only when the game is being simulated
			fpsLogger.log();
		}
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

	public GameScreen getGame() {
		return game;
	}
}
