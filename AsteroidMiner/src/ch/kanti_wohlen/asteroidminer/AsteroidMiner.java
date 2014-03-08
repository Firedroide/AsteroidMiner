package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.audio.MusicPlayer;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer;
import ch.kanti_wohlen.asteroidminer.screen.GameOverScreen;
import ch.kanti_wohlen.asteroidminer.screen.GameScreen;
import ch.kanti_wohlen.asteroidminer.screen.MenuScreen;
import ch.kanti_wohlen.asteroidminer.screen.PauseScreen;
import ch.kanti_wohlen.asteroidminer.screen.ScoreScreen;
import ch.kanti_wohlen.asteroidminer.screen.ScreenSwitchManager;
import ch.kanti_wohlen.asteroidminer.screen.SettingsScreen;
import ch.kanti_wohlen.asteroidminer.screen.TutorialScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class AsteroidMiner extends Game {

	public static AsteroidMiner INSTANCE;
	private final GameLauncher launcher;
	private final int targetTickRate;

	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	private PauseScreen pauseScreen;
	private GameOverScreen gameOverScreen;
	private ScoreScreen scoreScreen;
	private SettingsScreen settingsScreen;
	private TutorialScreen tutorialScreen;
	private ScreenSwitchManager switchManager;

	private FPSLogger fpsLogger;
	private SpriteBatch batch;
	private TaskScheduler scheduler;

	/**
	 * Constructs the main game class.
	 * 
	 * @param launcher
	 *            the launcher native to each platform. Used to perform Facebook publishing tasks.
	 * @param fpsAndTickRate
	 *            the target FPS and tick rate the game should be run at.
	 */
	public AsteroidMiner(GameLauncher gameLauncher, int fpsAndTickRate) {
		launcher = gameLauncher;
		targetTickRate = fpsAndTickRate;
		INSTANCE = this;
	}

	@Override
	public void create() {
		Textures.load();
		SoundPlayer.loadSounds();
		MusicPlayer.load();

		fpsLogger = new FPSLogger();
		batch = new SpriteBatch();
		scheduler = new TaskScheduler(targetTickRate);

		gameScreen = new GameScreen();
		menuScreen = new MenuScreen();
		pauseScreen = new PauseScreen();
		gameOverScreen = new GameOverScreen();
		scoreScreen = new ScoreScreen();
		settingsScreen = new SettingsScreen();
		tutorialScreen = new TutorialScreen();
		setScreen(menuScreen);
		MusicPlayer.start();

		Gdx.graphics.setVSync(false);

		final Preferences settings = Gdx.app.getPreferences("ch.kanti_wohlen.asteroidminer.settings");
		final float soundSetting = settings.getFloat("soundVolume", 1f);
		final float musicSetting = settings.getFloat("musicVolume", 0.8f);
		SoundPlayer.setVolume(MathUtils.clamp(soundSetting, 0f, 1f));
		MusicPlayer.setVolume(MathUtils.clamp(musicSetting, 0f, 1f));
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
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
	public void pause() {
		if (getScreen() == null) {
			setScreen(pauseScreen);
		}
	}

	@Override
	public void resume() {}

	public int getTargetTickRate() {
		return targetTickRate;
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public GameLauncher getGameLauncher() {
		return launcher;
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

	public ScoreScreen getScoreScreen() {
		return scoreScreen;
	}

	public SettingsScreen getSettingsScreen() {
		return settingsScreen;
	}

	public TutorialScreen getTutorialScreen() {
		return tutorialScreen;
	}

	public void switchScreenWithOverlay(Screen newScreen, Color overlayColor) {
		switchManager = new ScreenSwitchManager(this, newScreen, overlayColor);
	}
}
