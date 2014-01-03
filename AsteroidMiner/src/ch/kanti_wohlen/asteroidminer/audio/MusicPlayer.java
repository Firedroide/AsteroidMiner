package ch.kanti_wohlen.asteroidminer.audio;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

public class MusicPlayer {

	private static final float MUSIC_VOLUME = 0.2f;
	private static final float DEFAULT_USER_VOLUME = 0.8f;

	private static List<FileHandle> musicFiles;
	private static int currentIndex;
	private static Music currentMusic;
	private static float userVolume;

	private MusicPlayer() {}

	public static void load() {
		currentIndex = -1;
		userVolume = DEFAULT_USER_VOLUME;
		musicFiles = new LinkedList<FileHandle>();
		addMusic("Atmospheren Sound 1.ogg");
		addMusic("Atmospheren Sound 2.ogg");
	}

	public static void start() {
		if (musicFiles.size() == 0) return;

		if (currentMusic != null) {
			currentMusic.setOnCompletionListener(null);
			currentMusic.stop();
			currentMusic.dispose();
		}
		currentIndex = (currentIndex + 1) % musicFiles.size();
		currentMusic = Gdx.audio.newMusic(musicFiles.get(currentIndex));
		currentMusic.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(Music music) {
				start();
			}
		});
		currentMusic.setVolume(MUSIC_VOLUME * userVolume);
		currentMusic.play();
	}

	public static void stop() {
		if (currentMusic == null) return;
		currentMusic.setOnCompletionListener(null);
		currentMusic.stop();
		currentMusic.dispose();
	}

	public static void dispose() {
		stop();
		musicFiles.clear();
	}

	public static float getVolume() {
		return userVolume;
	}

	public static void setVolume(float newVolume) {
		userVolume = MathUtils.clamp(newVolume, 0f, 1f);
		currentMusic.setVolume(MUSIC_VOLUME * userVolume);
	}

	private static void addMusic(String fileName) {
		FileHandle fh = Gdx.files.internal("music/" + fileName);
		musicFiles.add(fh);
	}
}
