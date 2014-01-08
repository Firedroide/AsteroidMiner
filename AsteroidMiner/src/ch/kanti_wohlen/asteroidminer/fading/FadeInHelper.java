package ch.kanti_wohlen.asteroidminer.fading;

import ch.kanti_wohlen.asteroidminer.TaskScheduler;
import ch.kanti_wohlen.asteroidminer.TaskScheduler.Task;

public class FadeInHelper implements Runnable {

	private final Fadeable source;
	private final float time;
	private final Task task;
	private float currentTime;
	private final Runnable doneListener;

	public FadeInHelper(Fadeable fadeable, float startDelay, float fadeInTime) {
		this(fadeable, startDelay, fadeInTime, null);
	}

	public FadeInHelper(Fadeable fadeable, float startDelay, float fadeInTime, Runnable completionListener) {
		source = fadeable;
		time = fadeInTime;
		task = TaskScheduler.INSTANCE.runTaskRepeated(this, startDelay, 0, time);
		doneListener = completionListener;
	}

	@Override
	public void run() {
		currentTime += 1 * TaskScheduler.INSTANCE.TICK_TIME;
		source.setAlpha(currentTime / time);
		if (time - currentTime < TaskScheduler.INSTANCE.TICK_TIME) {
			if (doneListener != null) doneListener.run();
		}
	}

	public void cancel() {
		task.cancel();
	}
}
