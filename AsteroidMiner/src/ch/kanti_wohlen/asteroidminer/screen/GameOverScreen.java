package ch.kanti_wohlen.asteroidminer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameOverScreen extends OverlayScreen {

	private final Stage stage;
	private final Table table;

	public GameOverScreen() {
		super(Color.BLACK);

		stage = new Stage(width, height, true, batch);
		table = new Table();
		table.setBounds(0f, 0f, width, height);
		table.center();
		stage.addActor(table);

		Image gameOver = new Image(new Texture(Gdx.files.internal("graphics/gameOver.png")));
		table.add(gameOver).row();
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
		table.setBounds(0f, 0f, width, height);
		table.center();
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}
}
