package ch.kanti_wohlen.asteroidminer.screen;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;
import ch.kanti_wohlen.asteroidminer.GameMode;
import ch.kanti_wohlen.asteroidminer.fading.FadeInHelper;
import ch.kanti_wohlen.asteroidminer.fading.FadeOutHelper;
import ch.kanti_wohlen.asteroidminer.fading.Fadeable;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MenuScreen extends OverlayScreen implements Fadeable {

	public static final float FADING_OUT_TIME = 1.2f;

	private final Skin skin;
	private final Stage stage;
	private final Table table;

	public MenuScreen() {
		super(OVERLAY_COLOR);

		skin = new Skin();
		skin.addRegions(new TextureAtlas("data/uiskin.atlas"));
		skin.load(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage(width, height, true, batch);
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		Image title = new Image(new Texture(Gdx.files.internal("graphics/logo.png")));
		table.add(title).padBottom(50f).colspan(2).row();

		TextButton singlePlayer_2 = new TextButton(GameMode.TIME_2_MIN.getName(), skin);
		singlePlayer_2.addListener(new GameLauncher(GameMode.TIME_2_MIN));

		TextButton singlePlayer_5 = new TextButton(GameMode.TIME_5_MIN.getName(), skin);
		singlePlayer_5.addListener(new GameLauncher(GameMode.TIME_5_MIN));
		table.add(singlePlayer_2).padRight(5f).right();
		table.add(singlePlayer_5).left().row().padTop(10f);

		TextButton singlePlayer_endless = new TextButton(GameMode.ENDLESS.getName(), skin);
		singlePlayer_endless.addListener(new GameLauncher(GameMode.ENDLESS));
		table.add(singlePlayer_endless).padTop(10f).colspan(2).row();

		TextButton highscores = new TextButton("Highscores", skin);
		highscores.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				AsteroidMiner.INSTANCE.getGameLauncher().refreshHighscores(new Runnable() {

					@Override
					public void run() {
						synchronized (AsteroidMiner.INSTANCE.getScreen()) {
							AsteroidMiner.INSTANCE.switchScreenWithOverlay(AsteroidMiner.INSTANCE.getScoreScreen(),
									Color.BLACK);
						}
					}
				});
				return false;
			}
		});
		table.add(highscores).padTop(30f).colspan(2).row();

		TextButton settings = new TextButton("Settings", skin);
		settings.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.switchScreenWithOverlay(game.getSettingsScreen(), Color.BLACK);
				return false;
			}
		});
		table.add(settings).padTop(10f).colspan(2).row();

		if (Gdx.app.getType() == ApplicationType.Desktop) {
			TextButton endGame = new TextButton("Quit Game", skin);
			endGame.addListener(new InputListener() {

				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					Gdx.app.exit();
					return false;
				}
			});
			table.add(endGame).padTop(30f).colspan(2).row();
		}
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		stage.setViewport(width, height, true);
		table.setBounds(0, 0, width, height);
		table.center();
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void show() {
		super.show();
		setAlpha(1f);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		for (Actor actor : table.getChildren()) {
			if (actor instanceof TextButton) {
				((TextButton) actor).getClickListener().cancel();
				((TextButton) actor).setChecked(false);
			}
		}
	}

	@Override
	public float getAlpha() {
		return overlay.getColor().a;
	}

	@Override
	public void setAlpha(float newAlpha) {
		final float newA = MathUtils.clamp(newAlpha, 0f, 1f);
		stage.getRoot().getColor().a = newA;
		overlay.setColor(1f, 1f, 1f, newA);
	}

	private class GameLauncher extends InputListener {

		private final GameMode mode;

		private GameLauncher(GameMode gameMode) {
			mode = gameMode;
		}

		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			final Preferences settings = Gdx.app.getPreferences("ch.kanti_wohlen.asteroidminer.settings");
			final boolean showTutorialScreen = settings.getBoolean("showTutorial", true);
			System.out.println(showTutorialScreen);

			new FadeOutHelper(game.getMenuScreen(), 0f, FADING_OUT_TIME, new Runnable() {

				@Override
				public void run() {

					if (showTutorialScreen) {
						game.setScreen(game.getTutorialScreen());
						game.getTutorialScreen().setAlpha(0f);
						game.getTutorialScreen().setGameMode(mode);
						new FadeInHelper(game.getTutorialScreen(), 0.2f, 0.5f);
					} else {
						game.setScreen(null);
					}
					batch.setColor(Color.WHITE); // Some child of the table seems not to reset the batch's color.
				}
			});

			if (showTutorialScreen) {
				game.getGameScreen().stopSpawning();
			} else {
				game.getGameScreen().startGame(mode);
			}
			return false;
		}
	}
}
