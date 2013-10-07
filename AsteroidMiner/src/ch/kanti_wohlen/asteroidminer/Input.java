package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;

public class Input implements InputProcessor {
	
	private final AsteroidMiner game;
	
	public Input(AsteroidMiner asteroidMiner) {
		game = asteroidMiner;
		Gdx.input.setInputProcessor(this);
	}
	
	public void onGameRunning(SpaceShip ship) {
		boolean k = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT);
		float factorSpeed = k ? 160f : 80f;
		float factorTurn = k ? 0.05f : 0.125f;
		factorSpeed *= ship.getPhysicsBody().getMass();
		factorTurn *= ship.getPhysicsBody().getMass() * ship.getPhysicsBody().getMass();
		
		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
			float x = -MathUtils.sin(ship.getPhysicsBody().getAngle()) * factorSpeed;
			float y = MathUtils.cos(ship.getPhysicsBody().getAngle()) * factorSpeed;
			ship.getPhysicsBody().applyForceToCenter(x, y);
		}
		
		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			ship.getPhysicsBody().applyAngularImpulse(factorTurn);
		}
		if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
			ship.getPhysicsBody().applyAngularImpulse(-factorTurn);
		}
		
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			ship.fireLaser();
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE) {
			if (game.getScreen() == game.getScreens().GAME_SCREEN) {
				game.setScreen(game.getScreens().PAUSE_SCREEN);
			} else if (game.getScreen() == game.getScreens().PAUSE_SCREEN) {
				game.setScreen(game.getScreens().GAME_SCREEN);
			}
		}
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
