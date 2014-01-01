package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.audio.MusicPlayer;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer;
import ch.kanti_wohlen.asteroidminer.screen.GameOverScreen;
import ch.kanti_wohlen.asteroidminer.screen.GameScreen;
import ch.kanti_wohlen.asteroidminer.screen.MenuScreen;
import ch.kanti_wohlen.asteroidminer.screen.PauseScreen;
import ch.kanti_wohlen.asteroidminer.screen.ScreenSwitchManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AsteroidMiner extends Game {

	public static AsteroidMiner INSTANCE;
	private final GameLauncher launcher;

	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	private PauseScreen pauseScreen;
	private GameOverScreen gameOverScreen;
	private ScreenSwitchManager switchManager;

	private FPSLogger fpsLogger;
	private SpriteBatch batch;
	private TaskScheduler scheduler;

	/**
	 * Constructs the main game class.
	 * 
	 * @param launcher
	 *            the launcher native to each platform. Used to perform Facebook publishing tasks.
	 */
	public AsteroidMiner(GameLauncher gameLauncher) {
		launcher = gameLauncher;
		INSTANCE = this;
	}

	@Override
	public void create() {
		Textures.load(); // TODO: Loading system.
		SoundPlayer.loadSounds();
		MusicPlayer.load();

		fpsLogger = new FPSLogger();
		batch = new SpriteBatch();
		scheduler = TaskScheduler.INSTANCE;

		gameScreen = new GameScreen();
		menuScreen = new MenuScreen();
		pauseScreen = new PauseScreen();
		gameOverScreen = new GameOverScreen();
		setScreen(menuScreen);
		MusicPlayer.start();

		Gdx.graphics.setVSync(false);
	}

	@Override
	public void dispose() {
		scheduler.dispose();

		gameScreen.dispose();
		menuScreen.dispose();
		pauseScreen.dispose();
		gameOverScreen.dispose();
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
		gameScreen.render();

		// Render the currently active screen, if any.
		super.render();

		// If the game is not paused, let the game tick.
		if (getScreen() != pauseScreen) {
			gameScreen.tick(Gdx.graphics.getDeltaTime());

			// Execute scheduled tasks
			scheduler.onGameTick();

			// Log FPS only when the game is being simulated
			fpsLogger.log();
		}

		// If switching the current screen, render the overlay over everything else
		if (switchManager != null) {
			switchManager.render(batch);
		}
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public MenuScreen getMenuScreen() {
		return menuScreen;
	}

	public PauseScreen getPauseScreen() {
		return pauseScreen;
	}

	public GameOverScreen getGameOverScreen() {
		return gameOverScreen;
	}

	public void switchScreenWithOverlay(Screen newScreen, Color overlayColor) {
		switchManager = new ScreenSwitchManager(this, newScreen, overlayColor);
	}
}
