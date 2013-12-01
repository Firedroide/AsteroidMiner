package ch.kanti_wohlen.asteroidminer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;

public class PauseScreen extends OverlayScreen {

	private final Stage stage;
	private final Table table;

	public PauseScreen(AsteroidMiner asteroidMiner) {
		super(asteroidMiner);

		Skin skin = new Skin();
		skin.addRegions(new TextureAtlas("data/uiskin.atlas"));
		skin.load(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage(width, height, true);
		table = new Table();
		table.setBounds(0f, 0f, width, height);
		table.center();
		stage.addActor(table);

		Image pauseText = new Image(new Texture(Gdx.files.internal("graphics/pauseText.png")));
		table.add(pauseText).row();

		// TODO: Debug...
		table.debug();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		game.getSpriteBatch().begin();
		stage.draw();
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
	public void pause() {}

	@Override
	public void resume() {}
}
