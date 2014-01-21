package ch.kanti_wohlen.asteroidminer.screen;

import ch.kanti_wohlen.asteroidminer.audio.MusicPlayer;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class SettingsScreen extends OverlayScreen {

	private final Stage stage;
	private final Skin skin;
	private final Table table;
	private final Texture settingsTextTexture;

	private Slider soundSlider;
	private Slider musicSlider;
	private CheckBox tutorialCheckBox;

	public SettingsScreen() {
		super(OVERLAY_COLOR);

		skin = new Skin();
		skin.addRegions(new TextureAtlas("data/uiskin.atlas"));
		skin.load(Gdx.files.internal("data/uiskin.json"));
		settingsTextTexture = new Texture(Gdx.files.internal("graphics/settingsText.png"));

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
		Table.drawDebug(stage);
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

		final Preferences settings = Gdx.app.getPreferences("ch.kanti_wohlen.asteroidminer.settings");
		final float soundSetting = settings.getFloat("soundVolume", 1f);
		final float musicSetting = settings.getFloat("musicVolume", 0.8f);
		final boolean showTutorial = settings.getBoolean("showTutorial", true);

		table.clear();
		final Image settingsText = new Image(settingsTextTexture);
		settingsText.setColor(new Color(0.21f, 0.64f, 1f, 1f));
		table.add(settingsText).colspan(3).padBottom(40f).row();

		table.columnDefaults(0).spaceRight(20f).left();
		table.columnDefaults(1).width(200f);
		table.columnDefaults(2).spaceLeft(10f).width(50f);

		final Label soundLabel = new Label("Sound volume:", skin);
		soundSlider = new Slider(0f, 1f, 0.01f, false, skin);
		final Label soundValueLabel = new Label(toPercents(soundSetting), skin);
		soundSlider.setValue(soundSetting);
		soundSlider.addListener(new LabelUpdater(soundValueLabel));
		table.add(soundLabel);
		table.add(soundSlider).fillX();
		table.add(soundValueLabel);
		table.row();

		final Label musicLabel = new Label("Music volume:", skin);
		musicSlider = new Slider(0f, 1f, 0.01f, false, skin);
		final Label musicValueLabel = new Label(toPercents(musicSetting), skin);
		musicSlider.setValue(musicSetting);
		musicSlider.addListener(new LabelUpdater(musicValueLabel));
		table.add(musicLabel);
		table.add(musicSlider).fillX();
		table.add(musicValueLabel);
		table.row();
		table.add().height(20f).row();

		final Label tutorialLabel = new Label("Show tutorial?", skin);
		tutorialCheckBox = new CheckBox("", skin);
		tutorialCheckBox.setChecked(showTutorial);
		table.add(tutorialLabel);
		table.add(tutorialCheckBox).width(20f).right();
		table.row();
		table.add().height(30f).row();

		TextButton applyButton = new TextButton("Apply", skin);
		applyButton.pad(5f);
		applyButton.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				applySettings();
				game.switchScreenWithOverlay(game.getMenuScreen(), Color.BLACK);
				return true;
			}
		});

		TextButton backButton = new TextButton("Cancel", skin);
		backButton.pad(5f);
		backButton.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.switchScreenWithOverlay(game.getMenuScreen(), Color.BLACK);
				return true;
			}
		});
		HorizontalGroup buttons = new HorizontalGroup();
		buttons.setSpacing(10f);
		buttons.addActor(applyButton);
		buttons.addActor(backButton);
		table.add(buttons).colspan(3).right().row();
	}

	private void applySettings() {
		final Preferences settings = Gdx.app.getPreferences("ch.kanti_wohlen.asteroidminer.settings");
		final float soundSetting = MathUtils.clamp(soundSlider.getValue(), 0f, 1f);
		final float musicSetting = MathUtils.clamp(musicSlider.getValue(), 0f, 1f);
		final boolean showTutorial = tutorialCheckBox.isChecked();

		SoundPlayer.setVolume(soundSetting);
		MusicPlayer.setVolume(musicSetting);

		settings.putFloat("soundVolume", soundSetting);
		settings.putFloat("musicVolume", musicSetting);
		settings.putBoolean("showTutorial", showTutorial);
		settings.flush();
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	private static String toPercents(float value) {
		final float percentValue = MathUtils.round(MathUtils.clamp(value, 0f, 1f) * 100f);
		return String.valueOf(percentValue) + " %";
	}

	private static class LabelUpdater implements EventListener {

		private final Label label;

		private LabelUpdater(Label labelToUpdate) {
			label = labelToUpdate;
		}

		@Override
		public boolean handle(Event event) {
			final Slider slider = (Slider) event.getListenerActor();
			label.setText(toPercents(slider.getValue()));
			return false;
		}
	}
}
