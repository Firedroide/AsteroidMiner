package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer.SoundEffect;
import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;

public class LocalInput implements InputProcessor {

	private final AsteroidMiner main;
	private float thrusterVolume;
	private boolean thrusterPlaying;
	private boolean thrusterPanUp;

	public LocalInput() {
		main = AsteroidMiner.INSTANCE;
	}

	public void onGameRunning(LocalPlayer player) {
		boolean k = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT);
		float factorSpeed = k ? 160f : 80f;
		factorSpeed *= player.getSpaceShip().getSpeed();
		float factorTurn = k ? 0.05f : 0.125f;
		SpaceShip ship = player.getSpaceShip();

		factorSpeed *= ship.getPhysicsBody().getMass();
		factorTurn *= ship.getPhysicsBody().getMass() * ship.getPhysicsBody().getMass();

		thrusterVolume *= 0.95f;
		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
			float x = -MathUtils.sin(ship.getPhysicsBody().getAngle()) * factorSpeed;
			float y = MathUtils.cos(ship.getPhysicsBody().getAngle()) * factorSpeed;
			ship.getPhysicsBody().applyForceToCenter(x, y, true);
			thrusterVolume = Math.min(thrusterVolume + 0.01f, 0.2f);
		}
		if (thrusterVolume > 0.01f && !thrusterPlaying) {
			SoundPlayer.playSound(SoundEffect.THRUSTER, thrusterVolume, 1f, 0f, true);
			thrusterPlaying = true;
		} else if (thrusterVolume < 0.01f && thrusterPlaying) {
			SoundPlayer.stopSound(SoundEffect.THRUSTER);
			thrusterPlaying = false;
		}
		SoundEffect.THRUSTER.setVolume(thrusterVolume);

		if (thrusterPanUp && !k) {
			SoundEffect.THRUSTER.setPitch(1f);
			thrusterPanUp = false;
		} else if (!thrusterPanUp && k) {
			SoundEffect.THRUSTER.setPitch(1.015f);
			thrusterPanUp = true;
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
				main.setScreen(main.getPauseScreen());
			} else if (main.getScreen() == main.getPauseScreen()) {
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
