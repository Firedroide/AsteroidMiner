package ch.kanti_wohlen.asteroidminer;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TaskScheduler {

	/**
	 * The current and only instance of TaskScheduler.<br>
	 * Is never <code>null</code>.
	 */
	public static final TaskScheduler INSTANCE = new TaskScheduler();
	private static final float TICKS_PER_SECOND = 60f;

	/*
	 * At 60 ticks per seconds, this will be able to count up for about 414.2 days
	 * At 4000 ticks per second, this would still be able to count up for about 6.21 days
	 */
	private int currentTick;
	private final Map<Integer, Runnable[]> tasks;

	private TaskScheduler() {
		currentTick = 0;
		tasks = new TreeMap<Integer, Runnable[]>();
	}

	public void dispose() {
		tasks.clear();
	}

	/**
	 * Run task after rendering and after simulating the world.<br>
	 * This way the Runnable can be executed safely.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. Won't add to queue if <code>null</code>
	 */
	public void runTask(Runnable r) {
		if (r == null) return;
		addTask(0, r);
	}

	/**
	 * Runs a task after a delay in seconds.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. Won't add to queue if <code>null</code>
	 * @param delay
	 *            the time in seconds until the method should be called
	 */
	public void runTaskLater(Runnable r, float delay) {
		if (r == null) return;
		int executionTime = currentTick + Math.round(TICKS_PER_SECOND * delay);
		addTask(executionTime, r);
	}

	/**
	 * Runs a task after a given delay in game ticks.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. Won't add to queue if <code>null</code>
	 * @param delay
	 *            the time in game ticks until the method should be called
	 */
	public void runTaskLater(Runnable r, int tickDelay) {
		if (r == null) return;
		int executionTime = currentTick + tickDelay;
		addTask(executionTime, r);
	}

	private void addTask(int when, Runnable what) {
		if (tasks.containsKey(when)) {
			Runnable[] oldArr = tasks.get(when);
			Runnable[] newArr = new Runnable[oldArr.length + 1];
			System.arraycopy(oldArr, 0, newArr, 0, oldArr.length);
			newArr[oldArr.length] = what;
			tasks.put(when, newArr);
		} else {
			tasks.put(when, new Runnable[] {what});
		}
	}

	/**
	 * Method to actually execute the stored Runnables.<br>
	 * Should only ever be called once in GameScreen.
	 */
	public void onGameTick() {
		++currentTick;
		Iterator<Entry<Integer, Runnable[]>> i = tasks.entrySet().iterator();

		while (i.hasNext()) {
			Entry<Integer, Runnable[]> entry = i.next();

			if (entry.getKey() <= currentTick) {
				for (Runnable task : entry.getValue()) {
					task.run();
				}
				i.remove();
			} else {
				break;
			}
		}
	}
}
