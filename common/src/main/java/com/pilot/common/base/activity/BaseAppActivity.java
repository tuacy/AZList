package com.pilot.common.base.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pilot.common.R;
import com.pilot.common.log.PilotLog;

@SuppressLint("Registered")
public class BaseAppActivity extends AppCompatActivity {

	protected Activity mActivity;
	protected Context mContext;
	private   Handler mMainHandler;

	public enum TransitionMode {
		LEFT,
		RIGHT,
		TOP,
		BOTTOM,
		SCALE,
		FADE,
		ZOOM
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (null != getOverridePendingTransitionMode()) {
			switch (getOverridePendingTransitionMode()) {
				case LEFT:
					overridePendingTransition(R.anim.left_in, R.anim.left_out);
					break;
				case RIGHT:
					overridePendingTransition(R.anim.right_in, R.anim.right_out);
					break;
				case TOP:
					overridePendingTransition(R.anim.top_in, R.anim.top_out);
					break;
				case BOTTOM:
					overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
					break;
				case SCALE:
					overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
					break;
				case FADE:
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					break;
				case ZOOM:
					overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
					break;
			}
		}
		super.onCreate(savedInstanceState);
		mContext = this;
		mActivity = this;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(int id) {
		return (T) findViewById(id);
	}

	private Fragment mCurrentFragment;

	protected void showFragment(int layoutId, Class<? extends Fragment> clz) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		try {
			Fragment f;
			if ((f = fm.findFragmentByTag(clz.getName())) == null) {
				f = clz.newInstance();
				ft.add(layoutId, f, clz.getName());
			}
			ft.show(f).commit();
		} catch (Exception e) {
			PilotLog.e(BaseAppActivity.class, e, "Cannot get new instance of %s . Throw: %s. Message: %s", clz.getName(), e,
					   e.getMessage());
		}
	}

	protected void replaceFragment(int layoutId, Class<? extends Fragment> clz) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		try {
			Fragment f;
			if ((f = fm.findFragmentByTag(clz.getName())) == null) {
				f = clz.newInstance();
			}
			ft.replace(layoutId, f, clz.getName()).show(f).commit();
		} catch (Exception e) {
			PilotLog.e(BaseAppActivity.class, e, "Cannot get new instance of %s . Throw: %s. Message: %s", clz.getName(), e,
					   e.getMessage());
		}
	}

	protected void replaceFragment(int layoutId, Class<? extends Fragment> clz, Bundle args) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		try {
			Fragment f;
			if ((f = fm.findFragmentByTag(clz.getName())) == null) {
				f = clz.newInstance();
			}
			f.setArguments(args);
			ft.replace(layoutId, f, clz.getName()).show(f).commit();
		} catch (Exception e) {
			PilotLog.e(BaseAppActivity.class, e, "Cannot get new instance of %s . Throw: %s. Message: %s", clz.getName(), e,
					   e.getMessage());
		}
	}

	protected void addFragment(int layoutId, Class<? extends Fragment> clz) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		try {
			Fragment f = clz.newInstance();
			ft.add(layoutId, f, clz.getName()).show(f).commit();
		} catch (Exception e) {
			PilotLog.e(BaseAppActivity.class, e, "Cannot get new instance of %s . Throw: %s. Message: %s", clz.getName(), e,
					   e.getMessage());
		}
	}

	protected void addFragment(int layoutId, Class<? extends Fragment> clz, int enterAnim, int exitAnim) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		try {
			Fragment f = clz.newInstance();
			ft.add(layoutId, f, clz.getName()).setCustomAnimations(enterAnim, exitAnim).show(f).commit();
		} catch (Exception e) {
			PilotLog.e(BaseAppActivity.class, e, "Cannot get new instance of %s . Throw: %s. Message: %s", clz.getName(), e,
					   e.getMessage());
		}
	}

	protected void removeFragment(int layoutId) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment f = fm.findFragmentById(layoutId);
		if (f != null) {
			ft.remove(f).commit();
		}
	}

	protected void removeFragment(Class<? extends Fragment> clz) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment f = fm.findFragmentByTag(clz.getName());
		if (f != null) {
			ft.remove(f).commit();
		}
	}

	protected void hideFragment(int layoutId) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment f = fm.findFragmentById(layoutId);
		if (f != null) {
			ft.hide(f).commit();
		}
	}

	protected void hideFragment(Class<? extends Fragment> clz) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment f = fm.findFragmentByTag(clz.getName());
		if (f != null) {
			ft.hide(f).commit();
		}
	}

	protected Fragment getFragmentById(int layoutId) {
		FragmentManager fm = getFragmentManager();
		return fm.findFragmentById(layoutId);
	}

	protected void switchFragment(int layoutId, Fragment fragment) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if (mCurrentFragment == null) {
			ft.add(layoutId, fragment).commit();
		} else if (mCurrentFragment != fragment) {
			if (!fragment.isAdded()) {
				ft.hide(mCurrentFragment).add(layoutId, fragment).commit();
			} else {
				ft.hide(mCurrentFragment).show(fragment).commit();
			}
		}
		mCurrentFragment = fragment;
	}

	/**
	 * set the activity override pending transition mode
	 */
	protected TransitionMode getOverridePendingTransitionMode() {
		return null;
	}

	protected Handler getMainHandler() {
		if (mMainHandler == null) {
			mMainHandler = new Handler(Looper.getMainLooper());
		}
		return mMainHandler;
	}
}
