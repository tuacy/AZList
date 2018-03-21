package com.pilot.common.log;

import android.support.annotation.NonNull;

import com.pilot.common.log.printer.Printer;


public class PilotLog {

	private static LoggerImpl mLogger = new LoggerImpl();


	public static void setCustomLogger(@NonNull Printer logger) {
		mLogger.setCustomLogger(logger);
	}

	public static void setDebugEnabled(boolean enable) {
		mLogger.setDebugEnabled(enable);
	}

	public static boolean isDebugEnabled() {
		return mLogger.isDebugEnabled();
	}


	public static Logger methodCount(int methodCount) {
		return mLogger.methodCount(methodCount);
	}

	public static Logger title(String title) {
		return mLogger.title(title);
	}

	public static Logger threadInfo(boolean display) {
		return mLogger.threadInfo(display);
	}


	public static void tuacy(String msg) {
		mLogger.tuacy(msg);
	}

	public static void d(String tag, String msg) {
		mLogger.d(tag, msg);
	}

	public static void d(String tag, String format, Object... args) {
		mLogger.d(tag, format, args);
	}

	public static void d(Class<?> clazz, String format, Object... args) {
		mLogger.d(clazz, format, args);
	}

	public static void e(String tag, String msg) {
		mLogger.e(tag, msg);
	}

	public static void e(String tag, String format, Object... args) {
		mLogger.e(tag, format, args);
	}

	public static void e(Class<?> clazz, String format, Object... args) {
		mLogger.e(clazz, format, args);
	}

	public static void e(String tag, Throwable t, String format, Object... args) {
		mLogger.e(tag, t, format, args);
	}

	public static void e(Class<?> clazz, Throwable t, String format, Object... args) {
		mLogger.e(clazz, t, format, args);
	}

	public static void w(String tag, String msg) {
		mLogger.w(tag, msg);
	}

	public static void w(String tag, String format, Object... args) {
		mLogger.w(tag, format, args);
	}

	public static void w(Class<?> clazz, String format, Object... args) {
		mLogger.w(clazz, format, args);
	}

	public static void i(String tag, String msg) {
		mLogger.i(tag, msg);
	}

	public static void i(String tag, String format, Object... args) {
		mLogger.i(tag, format, args);
	}

	public static void i(Class<?> clazz, String format, Object... args) {
		mLogger.i(clazz, format, args);
	}

	public static void v(String tag, String msg) {
		mLogger.v(tag, msg);
	}

	public static void v(String tag, String format, Object... args) {
		mLogger.v(tag, format, args);
	}

	public static void v(Class<?> clazz, String format, Object... args) {
		mLogger.v(clazz, format, args);
	}

	public static void wtf(String tag, String msg) {
		mLogger.wtf(tag, msg);
	}

	public static void wtf(String tag, String format, Object... args) {
		mLogger.wtf(tag, format, args);
	}

	public static void wtf(Class<?> clazz, String format, Object... args) {
		mLogger.wtf(clazz, format, args);
	}

	public static void json(String tag, String json) {
		mLogger.json(tag, json);
	}

	public static void json(Class<?> clazz, String json) {
		mLogger.json(clazz, json);
	}

	public static void xml(String tag, String xml) {
		mLogger.xml(tag, xml);
	}

	public static void xml(Class<?> clazz, String xml) {
		mLogger.xml(clazz, xml);
	}
}
