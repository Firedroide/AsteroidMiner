package ch.kanti_wohlen.asteroidminer.screen;

import java.awt.Label;

import sun.swing.BakedArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;

public class ScoreScreen extends OverlayScreen {

	private final Stage stage;
	private Table table;
	
	public ScoreScreen(AsteroidMiner asteroidMiner) {
		super(asteroidMiner);
		
		stage = new Stage(width, height, true);
	}

	@Override
	public void show() {
		//TODO
		super.show();
		
		Skin skin = new Skin();
		skin.addRegions(new TextureAtlas("data/uiskin.atlas"));
		skin.load(Gdx.files.internal("data/uiskin.json"));
		
		table = new Table(skin);
		table.center();
		table.setBounds(0f, 0f, width, height);
		stage.addActor(table); 
		
		String game1Highscore = String.valueOf(/*erster*/null);
		Label label1Highscore = new Label("Erster: ",game1Highscore);
		table.add(label1Highscore).row();
		
		String game2Highscore = String.valueOf(/*zweiter*/null);
		Label label2Highscore = new Label("Zweiter: ",game2Highscore);
		table.add(label2Highscore).row();
		
		String game3Highscore = String.valueOf(/*dritter*/null);
		Label label3Highscore = new Label("Dritter: ",game3Highscore);
		table.add(label3Highscore).row();
		
		TextButton backButton = new TextButton("Zurück zum Hauptmenü", skin);
		backButton.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.getScreen(new MenuScreen(astroidMiner));
				game.setScreen(null);
				return false;
			}
		});
		
		table.add(backButton);
	}
	
	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

}
