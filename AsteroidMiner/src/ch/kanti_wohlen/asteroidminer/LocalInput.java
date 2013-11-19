package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;

public class LocalInput implements InputProcessor {

	private final AsteroidMiner main;

	public LocalInput(AsteroidMiner asteroidMiner) {
		main = asteroidMiner;
	}

	public void onGameRunning(LocalPlayer player) {
		boolean k = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT);
		float factorSpeed = k ? 160f : 80f;
		factorSpeed *= player.getSpaceShip().getSpeed();
		float factorTurn = k ? 0.05f : 0.125f;
		SpaceShip ship = player.getSpaceShip();

		factorSpeed *= ship.getPhysicsBody().getMass();
		factorTurn *= ship.getPhysicsBody().getMass() * ship.getPhysicsBody().getMass();

		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
			float x = -MathUtils.sin(ship.getPhysicsBody().getAngle()) * factorSpeed;
			float y = MathUtils.cos(ship.getPhysicsBody().getAngle()) * factorSpeed;
			ship.getPhysicsBody().applyForceToCenter(x, y, true);
		}

		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			ship.getPhysicsBody().applyAngularImpulse(factorTurn, true);
		}
		if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
			ship.getPhysicsBody().applyAngularImpulse(-factorTurn, true);
		}

		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			ship.fireLaser();
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE) {
			if (main.getScreen() == null) {
				main.setScreen(main.getScreens().PAUSE_SCREEN);
			} else if (main.getScreen() == main.getScreens().PAUSE_SCREEN) {
				main.setScreen(null);
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
