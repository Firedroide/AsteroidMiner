package ch.kanti_wohlen.asteroidminer.screen;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ScoreScreen extends OverlayScreen {

	private final Stage stage;
	private final Skin skin;
	private final Table table;

	public ScoreScreen() {
		super(Color.BLACK);

		skin = new Skin();
		skin.addRegions(new TextureAtlas("data/uiskin.atlas"));
		skin.load(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage(width, height, true, batch);
		table = new Table(skin);
		table.center();
		table.setBounds(0f, 0f, width, height);
		stage.addActor(table);

		// TODO Display scores.

		TextButton backButton = new TextButton("Back to main menu", skin);
		backButton.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.switchScreenWithOverlay(game.getMenuScreen(), Color.BLACK);
				return true;
			}
		});
		table.add(backButton).padTop(20f).row();

		table.add(backButton);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void setAlpha(float newAlpha) {
		final float newA = MathUtils.clamp(newAlpha, 0f, 1f);
		stage.getRoot().getColor().a = newA;
		overlay.setColor(1f, 1f, 1f, newA);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		AsteroidMiner.INSTANCE.getGameScreen().reset();
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

}
