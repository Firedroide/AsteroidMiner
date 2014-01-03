package ch.kanti_wohlen.asteroidminer.screen;

import ch.kanti_wohlen.asteroidminer.GameMode;
import ch.kanti_wohlen.asteroidminer.fading.FadeOutHelper;
import ch.kanti_wohlen.asteroidminer.fading.Fadeable;

import com.badlogic.gdx.Gdx;
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
		super(new Color(0.25f, 0.25f, 0.25f, 0.75f));

		skin = new Skin();
		skin.addRegions(new TextureAtlas("data/uiskin.atlas"));
		skin.load(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage(width, height, true, batch);
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		Image title = new Image(new Texture(Gdx.files.internal("graphics/logo.png")));
		table.add(title).padBottom(50f).colspan(2).row();

		TextButton singlePlayer_2 = new TextButton("2 Minute game", skin);
		singlePlayer_2.addListener(new GameLauncher(GameMode.TIME_2_MIN));

		TextButton singlePlayer_5 = new TextButton("5 Minute game", skin);
		singlePlayer_5.addListener(new GameLauncher(GameMode.TIME_5_MIN));
		table.add(singlePlayer_2).padRight(5f).right();
		table.add(singlePlayer_5).left().row().padTop(10f);

		TextButton singlePlayer_endless = new TextButton("Endless game", skin);
		singlePlayer_endless.addListener(new GameLauncher(GameMode.ENDLESS));
		table.add(singlePlayer_endless).padTop(10f).colspan(2).row();

		// TODO: Debug...
		// table.debug();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		stage.draw();
		Table.drawDebug(stage);
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
				((TextButton) actor).setChecked(false);
			}
		}
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

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
			new FadeOutHelper(game.getMenuScreen(), 0f, FADING_OUT_TIME, new Runnable() {

				@Override
				public void run() {
					game.setScreen(null);
					batch.setColor(Color.WHITE); // Some child of the table seems not to reset the batch's color.
				}
			});

			game.getGameScreen().startGame(mode);
			return false;
		}
	}
}
