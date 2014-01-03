package ch.kanti_wohlen.asteroidminer.audio;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

public class SoundPlayer {

	private static final float DEFAULT_USER_VOLUME = 1f;

	private static Map<SoundEffect, Sound> sounds;
	private static float userVolume;

	private SoundPlayer() {}

	public static void loadSounds() {
		sounds = new HashMap<SoundPlayer.SoundEffect, Sound>();
		userVolume = DEFAULT_USER_VOLUME;
		for (SoundEffect effect : SoundEffect.values()) {
			effect.load();
		}
	}

	public static void dispose() {
		for (Sound sound : sounds.values()) {
			sound.dispose();
		}
	}

	public static void playSound(SoundEffect soundEffect, float volume) {
		playSound(soundEffect, volume, 1f, 0f);
	}

	public static void playSound(SoundEffect soundEffect, float volume, float pitch, float pan) {
		playSound(soundEffect, volume, pitch, pan, false);
	}

	public static void playSound(SoundEffect soundEffect, float volume, float pitch, float pan, boolean looping) {
		Sound sound = sounds.get(soundEffect);
		final long id = sound.play(volume * userVolume, pitch, pan);
		sound.setLooping(id, looping);
		soundEffect.setSound(sound, id, volume, pitch, pan);
	}

	public static void stopSound(SoundEffect soundEffect) {
		if (soundEffect.currentSound != null) {
			soundEffect.currentSound.stop();
		}
	}

	public static float getVolume() {
		return userVolume;
	}

	public static void setVolume(float newVolume) {
		userVolume = MathUtils.clamp(newVolume, 0f, 1f);
		for (SoundEffect soundEffect : SoundEffect.values()) {
			if (soundEffect.currentSound != null) {
				soundEffect.currentSound.setVolume(soundEffect.currentID, soundEffect.currentVolume * userVolume);
			}
		}
	}

	public static void pauseAll() {
		for (SoundEffect soundEffect : SoundEffect.values()) {
			if (soundEffect.currentSound != null) {
				soundEffect.currentSound.pause();
			}
		}
	}

	public static void resumeAll() {
		for (SoundEffect soundEffect : SoundEffect.values()) {
			if (soundEffect.currentSound != null) {
				soundEffect.currentSound.resume();
			}
		}
	}

	public enum SoundEffect {
		LASER_SHOOT("LaserShoot.ogg"),
		POWER_UP_PICK_UP("PowerUpPickUp.ogg"),
		THRUSTER("Thrusters.ogg"),
		EXPLOSION("Explosion.ogg");

		private final String name;
		private long currentID;
		private Sound currentSound;
		private float currentVolume;
		private float currentPitch;
		private float currentPan;

		private SoundEffect(String fileName) {
			name = fileName;
		}

		private void load() {
			FileHandle fh = Gdx.files.internal("sounds/" + name);
			Sound sound = Gdx.audio.newSound(fh);
			sounds.put(this, sound);
		}

		private void setSound(Sound sound, long id, float volume, float pitch, float pan) {
			currentSound = sound;
			currentID = id;
			currentVolume = volume;
			currentPitch = pitch;
			currentPan = pan;
		}

		public float getVolume() {
			return currentVolume;
		}

		public void setVolume(float volume) {
			if (currentSound != null) {
				currentVolume = MathUtils.clamp(volume, 0f, 1f);
				currentSound.setVolume(currentID, currentVolume * userVolume);
			}
		}

		public float getPitch() {
			return currentPitch;
		}

		public void setPitch(float pitch) {
			if (currentSound != null) {
				currentPitch = MathUtils.clamp(pitch, 0.5f, 2f);
				currentSound.setPitch(currentID, currentPitch);
			}
		}

		public float getPan() {
			return currentPan;
		}

		public void setPan(float pan) {
			if (currentSound != null) {
				currentPan = MathUtils.clamp(pan, -1f, 1f);
				currentSound.setPan(currentID, currentPan, currentVolume * userVolume);
			}
		}
	}
}
