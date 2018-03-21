package com.pilot.common.base.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.pilot.common.log.PilotLog;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseApplication extends Application
	implements Application.ActivityLifecycleCallbacks, BaseAppCrashHandler.CrashListener {

	private static  BaseApplication sBaseApplication = null;
	protected final List<Activity>  mActivities      = new ArrayList<>();
	private         int             mActivityCount   = 0;
	protected Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		sBaseApplication = this;
		registerActivityLifecycleCallbacks(this);
		Thread.setDefaultUncaughtExceptionHandler(new BaseAppCrashHandler(this.getApplicationContext(), this));
	}

	protected abstract void initializeApplication();

	protected abstract void deInitializeApplication();

	protected abstract void onAppCrash(Context context, Thread thread, Throwable ex);

	public static BaseApplication getBaseApplication() {
		return sBaseApplication;
	}

	public List<Activity> getActivities() {
		return mActivities;
	}

	public Activity getTopActivity() {
		return mActivities.get(mActivities.size() - 1);
	}

	public void exit() {
		for (int i = mActivities.size() - 1; i >= 0; i--) {
			Activity activity = mActivities.get(i);
			activity.finish();
		}
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		if (0 == mActivityCount) {
			initializeApplication();
		}
		mActivities.add(activity);
		mActivityCount++;
	}

	@Override
	public void onActivityStarted(Activity activity) {

	}

	@Override
	public void onActivityResumed(Activity activity) {

	}

	@Override
	public void onActivityPaused(Activity activity) {

	}

	@Override
	public void onActivityStopped(Activity activity) {

	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		mActivities.remove(activity);
		mActivityCount--;
		if (0 == mActivityCount) {
			deInitializeApplication();
		}
	}

	@Override
	public void uncaughtException(Context context, Thread thread, Throwable ex) {
		printErrorInfo(context, thread, ex);
		onAppCrash(context, thread, ex);
	}

	private void printErrorInfo(Context context, Thread thread, Throwable ex) {
		String log = "StackTrace:\n";
		StackTraceElement[] elements = ex.getStackTrace();
		if (elements != null) {
			for (StackTraceElement element : elements) {
				log = log.concat(element.toString() + "\n");
			}
		}

		Throwable theCause = ex.getCause();
		if (theCause != null) {
			log = log.concat("Cause:\n");
			log = log.concat(theCause.toString() + "\n");

			elements = theCause.getStackTrace();
			if (elements != null) {
				log = log.concat("Cause Stack:\n");
				for (StackTraceElement element : elements) {
					log = log.concat(element.toString() + "\n");
				}
			}
		}
		PilotLog.threadInfo(true).e("onAppCrash", ex, log);
	}
}
