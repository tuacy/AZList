package com.pilot.common.base.config;

import android.content.Context;
import android.content.res.AssetManager;


import com.pilot.common.base.properties.BaseProperties;
import com.pilot.common.log.PilotLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public abstract class BasePropertiesConfig implements BaseProperties {

	private final Properties mProps = new Properties();

	protected abstract Context getContext();

	protected abstract String getPropertyFileName();

	protected BasePropertiesConfig() {
		initialize();
	}

	private void initialize() {
		Context context = getContext();
		InputStream in = null;
		try {
			AssetManager assetManager = context.getAssets();
			in = assetManager.open(getPropertyFileName());
			mProps.load(in);
		} catch (IOException e) {
			PilotLog.e(BasePropertiesConfig.class, e, "Cannot open: %s", getPropertyFileName());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	@Override
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	@Override
	public boolean getBoolean(String key, boolean defVal) {
		return Boolean.parseBoolean(mProps.getProperty(key, Boolean.toString(defVal)));
	}

	@Override
	public int getInteger(String key) {
		return getInteger(key, 0);
	}

	@Override
	public int getInteger(String key, int defVal) {
		return Integer.parseInt(mProps.getProperty(key, Integer.toString(defVal)));
	}

	@Override
	public short getShort(String key) {
		return getShort(key, 0);
	}

	@Override
	public short getShort(String key, int defVal) {
		return Short.parseShort(mProps.getProperty(key, Integer.toString(defVal)));
	}

	@Override
	public long getLong(String key) {
		return getLong(key, 0);
	}

	@Override
	public long getLong(String key, int defVal) {
		return Long.parseLong(mProps.getProperty(key, Integer.toString(defVal)));
	}

	@Override
	public String getString(String key) {
		return mProps.getProperty(key);
	}

	@Override
	public String getString(String key, String defVal) {
		return mProps.getProperty(key, defVal);
	}
}