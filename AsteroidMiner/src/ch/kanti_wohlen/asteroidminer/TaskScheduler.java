package ch.kanti_wohlen.asteroidminer;

import java.util.ArrayList;
import java.util.Iterator;

public class TaskScheduler {

	/**
	 * The current and only instance of TaskScheduler.<br>
	 * Is never <code>null</code>.
	 */
	public static TaskScheduler INSTANCE;
	public final float TICKS_PER_SECOND;
	public final float TICK_TIME;

	/*
	 * At 60 ticks per seconds, this will be able to count up for about 414.2 days
	 * At 4000 ticks per second, this would still be able to count up for about 6.21 days
	 */
	private int currentTick;
	private final ArrayList<Task> tasks;
	private final ArrayList<Task> taskBuffer;

	TaskScheduler(int targetTickRate) {
		INSTANCE = this;

		TICKS_PER_SECOND = (float) targetTickRate;
		TICK_TIME = 1f / TICKS_PER_SECOND;

		currentTick = 0;
		tasks = new ArrayList<Task>();
		taskBuffer = new ArrayList<Task>();
	}

	public void dispose() {
		tasks.clear();
	}

	/**
	 * Run task after rendering and after simulating the world.<br>
	 * This way the Runnable can be executed safely.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. May not be <code>null</code>
	 * @return the created and scheduled {@link Task}
	 */
	public Task runTask(Runnable r) {
		return runTaskRepeated(r, 0, 0, 0);
	}

	/**
	 * Runs a task after a delay in seconds.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. May not be <code>null</code>
	 * @param delay
	 *            the time in seconds until the <code>Runnable</code> should be called
	 * @return the created and scheduled {@link Task}
	 */
	public Task runTaskLater(Runnable r, float delay) {
		return runTaskRepeated(r, delay, 0f, 0f);
	}

	/**
	 * Runs a task after a given delay in game ticks.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. May not be <code>null</code>
	 * @param delay
	 *            the time in game ticks until the <code>Runnable</code> should be called
	 * @return the created and scheduled {@link Task}
	 */
	public Task runTaskLater(Runnable r, int tickDelay) {
		return runTaskRepeated(r, tickDelay, 0, 0);
	}

	/**
	 * Runs a task after a given delay and keeps executing it with a given amount of time between executions.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. May not be <code>null</code>
	 * @param delay
	 *            the time in seconds until the <code>Runnable</code> should be first called
	 * @param period
	 *            the time in seconds between the executions
	 * @return the created and scheduled {@link Task}
	 */
	public Task runTaskTimer(Runnable r, float delay, float period) {
		return runTaskRepeated(r, delay, period, 0f);
	}

	/**
	 * Runs a task after a given tick delay and keeps executing it with a given amount of ticks between executions.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. May not be <code>null</code>
	 * @param delay
	 *            the time in game ticks until the <code>Runnable</code> should be first called
	 * @param period
	 *            the time in game ticks between the executions
	 * @return the created and scheduled {@link Task}
	 */
	public Task runTaskTimer(Runnable r, int tickDelay, int tickPeriod) {
		return runTaskRepeated(r, tickDelay, tickPeriod, 0);
	}

	/**
	 * Runs a task after a given tick delay and keeps executing it with a given amount of time between executions
	 * for a given amount of time.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. May not be <code>null</code>
	 * @param delay
	 *            the time in seconds until the <code>Runnable</code> should be first called
	 * @param period
	 *            the time in seconds between the executions
	 * @param timespan
	 *            the time span in seconds during which the task should be executed
	 * @return the created and scheduled {@link Task}
	 */
	public Task runTaskRepeated(Runnable r, float delay, float period, float timespan) {
		Task t = new Task(r);
		t.executionTime = currentTick + Math.max(0, Math.round(TICKS_PER_SECOND * delay));
		t.period = Math.max(timespan <= 0f ? 0 : 1, Math.round(TICKS_PER_SECOND * period));
		if (timespan <= 0f) {
			t.lastExecutionTime = Integer.MAX_VALUE;
		} else {
			t.lastExecutionTime = t.executionTime + Math.max(0, Math.round(TICKS_PER_SECOND * timespan));
		}
		taskBuffer.add(t);
		return t;
	}

	/**
	 * Runs a task after a given tick delay and keeps executing it with a given amount of ticks between executions
	 * for a given amount of ticks.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. May not be <code>null</code>
	 * @param delay
	 *            the time in game ticks until the <code>Runnable</code> should be first called
	 * @param period
	 *            the time in game ticks between the executions
	 * @param timespan
	 *            the time span in game ticks during which the task should be executed
	 * @return the created and scheduled {@link Task}
	 */
	public Task runTaskRepeated(Runnable r, int tickDelay, int tickPeriod, int tickTimespan) {
		Task t = new Task(r);
		t.executionTime = currentTick + Math.max(0, tickDelay);
		t.period = Math.max(tickTimespan <= 0 ? 0 : 1, tickPeriod);
		if (tickTimespan <= 0) {
			t.lastExecutionTime = Integer.MAX_VALUE;
		} else {
			t.lastExecutionTime = t.executionTime + Math.max(0, tickTimespan);
		}
		taskBuffer.add(t);
		return t;
	}

	/**
	 * Runs a task after a given tick delay and keeps executing it with a given amount of time between executions
	 * for a given amount of time.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. May not be <code>null</code>
	 * @param delay
	 *            the time in seconds until the <code>Runnable</code> should be first called
	 * @param period
	 *            the time in seconds between the executions
	 * @param executions
	 *            the amount of times the task should be executed.<br>
	 *            Will return <code>null</code> if smaller or equal to zero
	 * @return the created and scheduled {@link Task} or null
	 */
	public Task runTaskMultipleTimes(Runnable r, float delay, float period, int executions) {
		if (executions <= 0) return null;
		Task t = new Task(r);
		t.executionTime = currentTick + Math.max(0, Math.round(TICKS_PER_SECOND * delay));
		t.period = Math.max(1, Math.round(TICKS_PER_SECOND * period));
		t.lastExecutionTime = t.executionTime + executions * t.period;
		taskBuffer.add(t);
		return t;
	}

	/**
	 * Runs a task multiple times after a given amount of time with a given amount of time between executions.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. May not be <code>null</code>
	 * @param delay
	 *            the time in game ticks until the <code>Runnable</code> should be first called
	 * @param period
	 *            the time in game ticks between the executions
	 * @param executions
	 *            the amount of times the task should be executed.<br>
	 *            Will return <code>null</code> if smaller or equal to zero
	 * @return the created and scheduled {@link Task} or null
	 */
	public Task runTaskMultipleTimes(Runnable r, int tickDelay, int tickPeriod, int executions) {
		if (executions <= 0) return null;
		Task t = new Task(r);
		t.executionTime = currentTick + Math.max(0, tickDelay);
		t.period = Math.max(1, tickPeriod);
		t.lastExecutionTime = t.executionTime + executions * t.period;
		taskBuffer.add(t);
		return t;
	}

	/**
	 * Method to actually execute the stored Runnables.<br>
	 * Should only ever be called once in GameScreen.
	 */
	public void onGameTick() {
		Iterator<Task> i = tasks.iterator();

		while (i.hasNext()) {
			Task task = i.next();

			if (task.cancelled) {
				i.remove();
				continue;
			} else if (task.executionTime > currentTick) {
				continue;
			}

			task.runnable.run();

			if (task.period > 0) {
				task.executionTime += task.period;
				if (task.executionTime > task.lastExecutionTime) {
					i.remove();
				}
			} else {
				i.remove();
			}
		}

		tasks.addAll(taskBuffer);
		taskBuffer.clear();
		++currentTick;
	}

	public final class Task {

		private final Runnable runnable;
		private int executionTime;
		private int period;
		private int lastExecutionTime;
		private boolean cancelled;

		private Task(Runnable theRunnable) {
			if (theRunnable == null) throw new NullPointerException("The runnable may never be null!");
			runnable = theRunnable;
		}

		public void cancel() {
			cancelled = true;
		}

		public Runnable getRunnable() {
			return runnable;
		}

		public int getNextExecutionTick() {
			return executionTime;
		}
	}
}
