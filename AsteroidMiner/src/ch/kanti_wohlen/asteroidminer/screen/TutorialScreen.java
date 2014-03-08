package ch.kanti_wohlen.asteroidminer.screen;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;
import ch.kanti_wohlen.asteroidminer.GameMode;
import ch.kanti_wohlen.asteroidminer.fading.FadeOutHelper;
import ch.kanti_wohlen.asteroidminer.fading.Fadeable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TutorialScreen extends AbstractScreen implements Fadeable {

	private static final int PAGE_COUNT = 2;
	private static final float BORDER = 20f;
	private static final float BORDER_SIDES = 40f;

	private final Stage stage;
	private final Skin skin;
	private final Table backgroundTable;
	private final Table foregroundTable;
	private final Texture tutorialTexture;

	private int page;
	private GameMode mode;

	public TutorialScreen() {
		skin = new Skin();
		skin.addRegions(new TextureAtlas("data/uiskin.atlas"));
		skin.load(Gdx.files.internal("data/uiskin.json"));
		tutorialTexture = new Texture(Gdx.files.internal("graphics/tutorial.png"));

		stage = new Stage(width, height, true, AsteroidMiner.INSTANCE.getSpriteBatch());
		backgroundTable = new Table(skin);
		backgroundTable.center();
		backgroundTable.setBounds(width * 0.15f, height * 0.08f, width * 0.7f, height * 0.84f);

		final Button backgroundButton = new Button(skin);
		backgroundButton.setDisabled(true);
		backgroundTable.add(backgroundButton).fill().expand().row();
		stage.addActor(backgroundTable);

		foregroundTable = new Table(skin);
		foregroundTable.center();
		foregroundTable.setBounds(width * 0.15f, height * 0.08f, width * 0.7f, height * 0.84f);
		foregroundTable.pad(BORDER);
		foregroundTable.padLeft(BORDER_SIDES);
		foregroundTable.padRight(BORDER_SIDES);
		stage.addActor(foregroundTable);
		foregroundTable.setZIndex(100);
	}

	@Override
	public void render(float delta) {
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		stage.setViewport(width, height, true);
		backgroundTable.setBounds(width * 0.15f, height * 0.08f, width * 0.7f, height * 0.84f);
		backgroundTable.center();
		foregroundTable.setBounds(width * 0.15f, height * 0.08f, width * 0.7f, height * 0.84f);
		foregroundTable.center();
	}

	@Override
	public void show() {
		super.show();
		Gdx.input.setInputProcessor(stage);
		stage.getRoot().setColor(Color.WHITE);
		page = 0;

		final Preferences settings = Gdx.app.getPreferences("ch.kanti_wohlen.asteroidminer.settings");
		settings.putBoolean("showTutorial", false);
		settings.flush();

		buildTable();
	}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

	public void setGameMode(GameMode gameMode) {
		mode = gameMode;
	}

	private void buildTable() {
		foregroundTable.clear();
		switch (page) {
		case 0:
			final TextureRegion controlsText = new TextureRegion(tutorialTexture, 0, 0, 185, 40);
			final Image controlsTextImage = new Image(controlsText);
			controlsTextImage.setColor(new Color(0.21f, 0.64f, 1f, 1f));
			foregroundTable.add(controlsTextImage).spaceBottom(30f);
			foregroundTable.row();
			foregroundTable.columnDefaults(0).spaceRight(40f);
			foregroundTable.columnDefaults(1).expandX();
			foregroundTable.columnDefaults(2).spaceLeft(40f);

			final HorizontalGroup wasdGroup = new HorizontalGroup();
			final TextureRegion wasd = new TextureRegion(tutorialTexture, 357, 0, 155, 145);
			final Image wasdImage = new Image(wasd);
			final Label wasdText = new Label("Move your spaceship by using WASD\n"
					+ "or the arrow keys. But watch out!\n"
					+ "Movement is relative to your spaceship's rotation,\n"
					+ "so up does not always mean up!", skin);
			wasdGroup.space(30f);
			wasdGroup.addActor(wasdImage);
			wasdGroup.addActor(wasdText);
			foregroundTable.add(wasdGroup).left().row();

			final HorizontalGroup mouseGroup = new HorizontalGroup();
			final TextureRegion mouse = new TextureRegion(tutorialTexture, 378, 150, 134, 140);
			final Image mouseImage = new Image(mouse);
			final Label mouseText = new Label("Press SHIFT to use your boosters\n\n"
					+ "Use the mouse to rotate your spaceship", skin);
			mouseGroup.space(60f);
			mouseGroup.addActor(mouseText);
			mouseGroup.addActor(mouseImage);
			foregroundTable.add(mouseGroup).right().row();

			final HorizontalGroup laserGroup = new HorizontalGroup();
			final TextureRegion laser = new TextureRegion(tutorialTexture, 381, 290, 131, 100);
			final Image laserImage = new Image(laser);
			final Label laserText = new Label("Press the left mouse button or the space bar\n"
					+ "to shoot your lasers. Use the lasers\n"
					+ "to shoot the asteroids that are\n"
					+ "dashing towards your spaceship!", skin);
			laserGroup.space(40f);
			laserGroup.addActor(laserImage);
			laserGroup.addActor(laserText);
			foregroundTable.add(laserGroup).left().row();
			break;
		case 1:
			final TextureRegion asteroidsText = new TextureRegion(tutorialTexture, 0, 45, 200, 40);
			final Image asteroidsTextImage = new Image(asteroidsText);
			asteroidsTextImage.setColor(new Color(0.21f, 0.64f, 1f, 1f));
			foregroundTable.add(asteroidsTextImage).spaceBottom(40f).row();

			final HorizontalGroup stoneAsteroidGroup = new HorizontalGroup();
			final TextureRegion stoneAsteroid = new TextureRegion(tutorialTexture, 0, 437, 83, 75);
			final Image stoneAsteroidImage = new Image(stoneAsteroid);
			final Label stoneAsteroidText = new Label("Stone asteroids are pretty easy to destroy,\n"
					+ "but they will split into two smaller\n"
					+ "asteroids when they're still large enough.", skin);
			stoneAsteroidGroup.space(40f);
			stoneAsteroidGroup.addActor(stoneAsteroidImage);
			stoneAsteroidGroup.addActor(stoneAsteroidText);
			foregroundTable.add(stoneAsteroidGroup).left().spaceBottom(15f).row();

			final HorizontalGroup iceAsteroidGroup = new HorizontalGroup();
			final TextureRegion iceAsteroid = new TextureRegion(tutorialTexture, 84, 437, 83, 75);
			final Image iceAsteroidImage = new Image(iceAsteroid);
			final Label iceAsteroidText = new Label("Ice asteroids cannot simply be shattered into pieces\n"
					+ "like those stone asteroids. Your lasers will actually melt\n"
					+ "the ice causing the asteroids to shrink until they disappear.", skin);
			iceAsteroidGroup.space(40f);
			iceAsteroidGroup.addActor(iceAsteroidText);
			iceAsteroidGroup.addActor(iceAsteroidImage);
			foregroundTable.add(iceAsteroidGroup).right().spaceBottom(15f).row();

			final HorizontalGroup metalAsteroidGroup = new HorizontalGroup();
			final TextureRegion metalAsteroid = new TextureRegion(tutorialTexture, 168, 437, 77, 75);
			final Image metalAsteroidImage = new Image(metalAsteroid);
			final Label metalAsteroidText = new Label("Metal are quite hard to destroy and contain small\n"
					+ "ore chunks which will be shot into all directions\n"
					+ "when the metal asteroid is destroyed.", skin);
			metalAsteroidGroup.space(40f);
			metalAsteroidGroup.addActor(metalAsteroidImage);
			metalAsteroidGroup.addActor(metalAsteroidText);
			foregroundTable.add(metalAsteroidGroup).left().spaceBottom(15f).row();

			final HorizontalGroup explosiveAsteroidGroup = new HorizontalGroup();
			final TextureRegion explosiveAsteroid = new TextureRegion(tutorialTexture, 247, 437, 82, 75);
			final Image explosiveAsteroidImage = new Image(explosiveAsteroid);
			final Label explosiveAsteroidText = new Label("You should watch out for these asteroids!\n"
					+ "They are structually instable and will immediately explode\n"
					+ "should a laser hit it or an unfortunate spaceship bump into it.", skin);
			explosiveAsteroidGroup.space(40f);
			explosiveAsteroidGroup.addActor(explosiveAsteroidText);
			explosiveAsteroidGroup.addActor(explosiveAsteroidImage);
			foregroundTable.add(explosiveAsteroidGroup).right().spaceBottom(15f).row();
			break;
		default:
			throw new IllegalStateException("Page number not defined.");
		}

		foregroundTable.add().expandY().expandX().row();
		final HorizontalGroup buttons = new HorizontalGroup();
		buttons.space(10f);
		if (page > 0) {
			final TextButton backButton = new TextButton("< Back", skin);
			backButton.addListener(new InputListener() {

				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					page -= 1;
					buildTable();
					return false;
				}
			});
			buttons.addActor(backButton);
		}
		if (page + 1 < PAGE_COUNT) {
			final TextButton nextButton = new TextButton("Next >", skin);
			nextButton.addListener(new InputListener() {

				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					page += 1;
					buildTable();
					return false;
				}
			});
			buttons.addActor(nextButton);
		} else {
			final TextButton doneButton = new TextButton("Continue", skin);
			doneButton.addListener(new InputListener() {

				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					new FadeOutHelper(AsteroidMiner.INSTANCE.getTutorialScreen(), 0f, 1f, new Runnable() {

						@Override
						public void run() {
							AsteroidMiner.INSTANCE.setScreen(null);
							AsteroidMiner.INSTANCE.getSpriteBatch().setColor(Color.WHITE);
							if (mode != null) {
								AsteroidMiner.INSTANCE.getGameScreen().startGame(mode);
							}
						}
					});
					return false;
				}
			});
			buttons.addActor(doneButton);
		}
		foregroundTable.add(buttons).bottom().right().row();
	}

	@Override
	public float getAlpha() {
		return stage.getRoot().getColor().a;
	}

	@Override
	public void setAlpha(float newAlpha) {
		final float newA = MathUtils.clamp(newAlpha, 0f, 1f);
		stage.getRoot().setColor(1f, 1f, 1f, newA);
	}
}
