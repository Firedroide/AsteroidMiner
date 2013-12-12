package ch.kanti_wohlen.asteroidminer.screen;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MenuScreen extends OverlayScreen {

	private final Stage stage;
	private final Table table;

	public MenuScreen(AsteroidMiner asteroidMiner) {
		super(asteroidMiner);

		Skin skin = new Skin();
		skin.addRegions(new TextureAtlas("data/uiskin.atlas"));
		skin.load(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage(width, height, true);
		table = new Table();
		table.setBounds(0f, 0f, width, height);
		table.center();
		stage.addActor(table);

		Image title = new Image(new Texture(Gdx.files.internal("graphics/logo.png")));
		table.add(title).padBottom(50f).row();

		TextButton singlePlayer = new TextButton("Single Player", skin);
		singlePlayer.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("DEBUG:", "Launched single player session.");
				game.getGameScreen().startGame();
				game.setScreen(null);
				return false;
			}
		});
		table.add(singlePlayer).row();

		// TODO: Debug...
		//table.debug();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		game.getSpriteBatch().begin();
		stage.draw();
		Table.drawDebug(stage);
		game.getSpriteBatch().end();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		stage.setViewport(width, height, true);
		table.setBounds(0, 0, width, height);
		table.center();
	}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
	}

	@Override
	public void show() {
		super.show();
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}
}
