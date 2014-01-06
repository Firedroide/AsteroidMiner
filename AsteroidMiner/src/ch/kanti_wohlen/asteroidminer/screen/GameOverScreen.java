package ch.kanti_wohlen.asteroidminer.screen;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;
import ch.kanti_wohlen.asteroidminer.TaskScheduler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameOverScreen extends OverlayScreen {

	private static final float SCREEN_SHOW_TIME = 5f;

	private final Skin skin;
	private final Stage stage;
	private final Table table;
	private final Label scoreLabel;

	public GameOverScreen() {
		super(Color.BLACK);

		skin = new Skin();
		skin.addRegions(new TextureAtlas("data/uiskin.atlas"));
		skin.load(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage(width, height, true, batch);
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		Image gameOver = new Image(new Texture(Gdx.files.internal("graphics/gameOver.png")));
		table.add(gameOver).colspan(4).padBottom(20f).row();

		table.add().fill();
		Label score1 = new Label("Your final score:", skin);
		score1.setColor(Color.RED);
		scoreLabel = new Label(String.valueOf(0), skin);
		scoreLabel.setColor(Color.RED);
		table.add(score1).padRight(5f);
		table.add(scoreLabel).center().width(20f);
		table.add().fill().row();
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
		table.setBounds(0f, 0f, width, height);
		table.center();
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
	}

	@Override
	public void show() {
		super.show();

		final int score = game.getGameScreen().getLocalPlayer().getScore();
		scoreLabel.setText(String.valueOf(score));

		TaskScheduler.INSTANCE.runTaskLater(new Runnable() {

			@Override
			public void run() {
				game.switchScreenWithOverlay(game.getScoreScreen(), Color.BLACK);
			}
		}, SCREEN_SHOW_TIME);

		AsteroidMiner.INSTANCE.getGameLauncher().setHighscore(score);
		AsteroidMiner.INSTANCE.getGameScreen().reset();
	}

	@Override
	public void setAlpha(float newAlpha) {
		final float newA = MathUtils.clamp(newAlpha, 0f, 1f);
		stage.getRoot().getColor().a = newA;
		overlay.setColor(1f, 1f, 1f, newA);
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}
}
