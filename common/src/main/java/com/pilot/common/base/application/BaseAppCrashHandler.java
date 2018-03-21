package com.pilot.common.base.application;

import android.content.Context;

import java.lang.Thread.UncaughtExceptionHandler;


public class BaseAppCrashHandler implements UncaughtExceptionHandler {

	private Context       mContext;
	private CrashListener mCrashListener;


	public BaseAppCrashHandler(Context context, CrashListener l) {
		super();
		mContext = context;
		mCrashListener = l;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (mCrashListener != null) {
			mCrashListener.uncaughtException(mContext, thread, ex);
		}
	}

	interface CrashListener {

		void uncaughtException(Context context, Thread thread, Throwable ex);
	}
}
