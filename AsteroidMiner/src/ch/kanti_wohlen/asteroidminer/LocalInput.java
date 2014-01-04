package ch.kanti_wohlen.asteroidminer;

import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer.SoundEffect;
import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class LocalInput implements InputProcessor {

	private final AsteroidMiner main;
	private float thrusterVolume;
	private boolean thrusterPlaying;
	private boolean thrusterPanUp;

	public LocalInput() {
		main = AsteroidMiner.INSTANCE;
	}

	public void onGameRunning(LocalPlayer player) {
		if (player.getSpaceShip().getHealth() == 0) return;

		boolean shift = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT);
		float factorSpeed = shift ? 160f : 80f;
		factorSpeed *= player.getSpaceShip().getSpeed();
		float factorTurn = shift ? 0.25f : 0.5f;
		final SpaceShip ship = player.getSpaceShip();
		final Body body = ship.getPhysicsBody();

		// Movement
		factorSpeed *= body.getMass();
		factorTurn *= body.getMass() * body.getMass();

		boolean moved = false;
		thrusterVolume *= 0.95f;
		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
			float x = MathUtils.sin(body.getAngle()) * factorSpeed;
			float y = MathUtils.cos(body.getAngle()) * factorSpeed;
			body.applyForceToCenter(-x, y, true);
			moved = true;
		}
		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			float x = MathUtils.cos(body.getAngle()) * factorSpeed * 0.6f;
			float y = -MathUtils.sin(body.getAngle()) * factorSpeed * 0.6f;
			body.applyForceToCenter(-x, y, true);
			moved = true;
		}
		if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
			float x = -MathUtils.cos(body.getAngle()) * factorSpeed * 0.6f;
			float y = MathUtils.sin(body.getAngle()) * factorSpeed * 0.6f;
			body.applyForceToCenter(-x, y, true);
			moved = true;
		}
		if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
			float x = -MathUtils.sin(body.getAngle()) * factorSpeed * 0.6f;
			float y = -MathUtils.cos(body.getAngle()) * factorSpeed * 0.6f;
			body.applyForceToCenter(-x, y, true);
			moved = true;
		}

		// Rotation
		final Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		final Vector2 spaceShipPos = main.getGameScreen().getLocationOnScreen(body);
		final float mouseAngle = spaceShipPos.sub(mousePos).angle();
		final float spaceShipAngle = 360f - ((body.getAngle() * MathUtils.radDeg + 270f) % 360f);
		float angularDiff = spaceShipAngle - mouseAngle;
		if (angularDiff > 180f) {
			angularDiff -= 360f;
		} else if (angularDiff < -180f) {
			angularDiff += 360f;
		}
		angularDiff = MathUtils.clamp(angularDiff, -10f, 10f) / 10f;
		final float velCorr = 4f * body.getAngularVelocity() * body.getMass();
		body.applyAngularImpulse(factorTurn * angularDiff - velCorr, true);

		// IMA FIRIN' MAH LAZORS!!
		if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isTouched()) {
			ship.fireLaser();
		}

		// Sound
		if (moved) {
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

		if (thrusterPanUp && !shift) {
			SoundEffect.THRUSTER.setPitch(1f);
			thrusterPanUp = false;
		} else if (!thrusterPanUp && shift) {
			SoundEffect.THRUSTER.setPitch(1.015f);
			thrusterPanUp = true;
		}

		if (Gdx.input.isKeyPressed(Keys.Q) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			body.applyAngularImpulse(factorTurn, true);
		}
		if (Gdx.input.isKeyPressed(Keys.E) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
			body.applyAngularImpulse(-factorTurn, true);
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
