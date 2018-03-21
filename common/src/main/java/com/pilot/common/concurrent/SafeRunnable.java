package com.pilot.common.concurrent;

public abstract class SafeRunnable implements Runnable {

	public enum RunnableState {
		STATE_COMPLETE,
		STATE_RETRY,
		STATE_INTERRUPT,
		STATE_FAILED
	}

	private static final int RETRY_LIMIT = 3;

	public SafeRunnable() {

	}

	protected abstract void onRun() throws Throwable;

	protected void onStart() {

	}

	protected void onComplete() {

	}

	protected void onInterrupt() {

	}

	protected void onFailed() {

	}

	protected abstract RunnableState getStateByThrowable(Throwable t);

	protected int getRetryLimit() {
		return RETRY_LIMIT;
	}

	/**
	 * Runs the job and catches any exception
	 *
	 * @param currentRunCount number of the Job ran.
	 * @return true if no need to retry, false otherwise.
	 */
	public final boolean safeRun(int currentRunCount) {
		boolean ret = true;
		RunnableState state = RunnableState.STATE_COMPLETE;

		onStart();
		try {
			onRun();
		} catch (Throwable t) {
			state = getStateByThrowable(t);
			if (RunnableState.STATE_RETRY == state) {
				if (currentRunCount < getRetryLimit()) {
					state = RunnableState.STATE_RETRY;
				} else {
					state = RunnableState.STATE_FAILED;
				}
			}
		} finally {
			try {
				switch (state) {
					case STATE_COMPLETE:
						onComplete();
						break;
					case STATE_RETRY:
						ret = false;
						break;
					case STATE_INTERRUPT:
						onInterrupt();
						break;
					case STATE_FAILED:
						onFailed();
					default:
						break;
				}
			} catch (Throwable ignored) {
				//ignored
			}
		}

		return ret;
	}

	@Override
	public void run() {
		int runCount = 0;
		while (!safeRun(runCount)) {
			runCount++;
		}
	}
}
