package ch.kanti_wohlen.asteroidminer.screen;

import java.util.List;

import ch.kanti_wohlen.asteroidminer.Pair;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class ScoreScreen extends OverlayScreen {

	private final Stage stage;
	private final Skin skin;
	private final Table table;

	public ScoreScreen() {
		super(OVERLAY_COLOR);

		skin = new Skin();
		skin.addRegions(new TextureAtlas("data/uiskin.atlas"));
		skin.load(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage(width, height, true, batch);
		table = new Table(skin);
		table.center();
		table.setBounds(0f, 0f, width, height);
		stage.addActor(table);
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

		table.clear();
		Image highscoreText = new Image(new Texture(Gdx.files.internal("graphics/highscoreText.png")));
		highscoreText.setColor(new Color(0.21f, 0.64f, 1f, 1f));
		table.add(highscoreText).colspan(2).padBottom(20f).row();

		final TextField header1;
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			header1 = new TextField("Rank", skin);
		} else {
			header1 = new TextField("Player", skin);
		}
		final TextField header2 = new TextField("Score", skin);
		header1.setDisabled(true);
		header1.setColor(0.6f, 0.8f, 1f, 1f);
		header2.setDisabled(true);
		header2.setColor(0.6f, 0.8f, 1f, 1f);
		table.add(header1).minWidth(250f);
		table.add(header2).minWidth(100f);
		table.row();

		List<Pair<String, Integer>> highscores = game.getGameLauncher().getHighscores();
		boolean lightColor = false;
		for (Pair<String, Integer> pair : highscores) {
			final TextField field1 = new TextField(pair.getKey(), skin);
			field1.setDisabled(true);
			final TextField field2 = new TextField(String.valueOf(pair.getValue()), skin);
			field2.setDisabled(true);
			if (!lightColor) {
				field1.setColor(0.7f, 0.7f, 0.7f, 1f);
				field2.setColor(0.7f, 0.7f, 0.7f, 1f);
			}

			table.add(field1).fillX();
			table.add(field2).fillX();
			table.row();
			lightColor = !lightColor;
		}

		if (Gdx.app.getType() == ApplicationType.WebGL) {
			// Ask user to post to feed
		}

		TextButton backButton = new TextButton("Back to main menu", skin);
		backButton.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.switchScreenWithOverlay(game.getMenuScreen(), Color.BLACK);
				return true;
			}
		});
		table.add(backButton).colspan(2).padTop(20f).row();
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}
}
