package com.pilot.common.poster;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * execute in background thread.
 */
public final class BackgroundPoster {


	private Executor mPoster;

	public BackgroundPoster(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
		mPoster = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
										 new LinkedBlockingQueue<Runnable>());
	}

	public void post(@NonNull Runnable r) {
		mPoster.execute(r);
	}
}
