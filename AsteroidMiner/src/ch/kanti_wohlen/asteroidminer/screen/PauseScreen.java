package ch.kanti_wohlen.asteroidminer.screen;

import ch.kanti_wohlen.asteroidminer.audio.MusicPlayer;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class PauseScreen extends OverlayScreen {

	private static final float MUSIC_VOLUME_MULTIPLIER = 0.2f;

	private final Skin skin;
	private final Stage stage;
	private final Table table;

	private float lastMusicVolume;

	public PauseScreen() {
		super(OVERLAY_COLOR);

		skin = new Skin();
		skin.addRegions(new TextureAtlas("data/uiskin.atlas"));
		skin.load(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage(width, height, true, batch);
		table = new Table();
		table.setBounds(0f, 0f, width, height);
		table.center();
		stage.addActor(table);

		Image pauseText = new Image(new Texture(Gdx.files.internal("graphics/pauseText.png")));
		table.add(pauseText).row();
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
		super.show();
		lastMusicVolume = MusicPlayer.getVolume();
		MusicPlayer.setVolume(lastMusicVolume * MUSIC_VOLUME_MULTIPLIER);
		SoundPlayer.pauseAll();
	}

	@Override
	public void hide() {
		MusicPlayer.setVolume(lastMusicVolume);
		SoundPlayer.resumeAll();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}
}
