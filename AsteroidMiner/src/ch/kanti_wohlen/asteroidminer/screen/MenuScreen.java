package ch.kanti_wohlen.asteroidminer.screen;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MenuScreen extends AbstractScreen {

	private final AsteroidMiner game;
	private final Stage stage;
	private final Table table;

	public MenuScreen(AsteroidMiner asteroidMiner) {
		game = asteroidMiner;
		width = 960;
		height = 640;

		Skin skin = new Skin();
		skin.addRegions(new TextureAtlas("data/uiskin.atlas"));
		skin.load(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage(width, height, true);
		table = new Table();
		table.setBounds(0f, 0f, width, height);
		table.center();
		stage.addActor(table);

		Label title = new Label("AsteroidMiner", skin);
		title.setFontScale(2f);
		title.setAlignment(1);
		table.add(title).width(400f).padBottom(50f).row();

		TextButton singlePlayer = new TextButton("Single Player", skin);
		singlePlayer.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("DEBUG:", "Launched single player session.");
				game.setScreen(game.getScreens().GAME_SCREEN);
				return false;
			}
		});
		table.add(singlePlayer).row();

		Gdx.input.setInputProcessor(stage);

		table.debug();
	}

	@Override
	public void render(float delta) {
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
	public void hide() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}
}
