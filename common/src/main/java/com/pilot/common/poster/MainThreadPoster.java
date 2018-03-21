package com.pilot.common.poster;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * execute in main thread.
 */
public final class MainThreadPoster {

	private final Executor mPoster;
	private Handler mHandler = new Handler(Looper.getMainLooper());

	public MainThreadPoster() {
		mPoster = new Executor() {
			@Override
			public void execute(@NonNull Runnable command) {
				mHandler.post(command);
			}
		};
	}

	public void post(Runnable r) {
		mPoster.execute(r);
	}
}