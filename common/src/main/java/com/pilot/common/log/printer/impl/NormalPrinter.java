package com.pilot.common.log.printer.impl;

import android.util.Log;

import com.pilot.common.log.bean.LogInfo;
import com.pilot.common.log.printer.Printer;


public class NormalPrinter implements Printer {

	private static NormalPrinter sInstance;

	private NormalPrinter() {
	}

	public static NormalPrinter getInstance() {
		if (sInstance == null) {
			synchronized (NormalPrinter.class) {
				if (sInstance == null) {
					sInstance = new NormalPrinter();
				}
			}
		}
		return sInstance;
	}

	@Override
	public void print(LogInfo logInfo) {
		doPrint(logInfo.getLogLevel(), logInfo.getTag(), logInfo.getMessage());
	}

	private void doPrint(LogInfo.LogLevel logLevel, String tag, String message) {
		switch (logLevel) {
			case ERROR:
				Log.e(tag, message);
				break;
			case INFO:
				Log.i(tag, message);
				break;
			case VERBOSE:
				Log.v(tag, message);
				break;
			case WARN:
				Log.w(tag, message);
				break;
			case ASSERT:
				Log.wtf(tag, message);
				break;
			case DEBUG:
				// Fall through, log debug by default
			default:
				Log.d(tag, message);
				break;
		}
	}
}
